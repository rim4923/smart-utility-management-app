using Microsoft.ML;
using Microsoft.ML.Data;
using Microsoft.ML.Transforms.TimeSeries;

public class TimeSeriesData
{
    public float Value { get; set; }
}

public class ForecastOutput
{
    [VectorType(3)]
    public float[] Forecasted { get; set; }

    [VectorType(3)]
    public float[] LowerBound { get; set; }

    [VectorType(3)]
    public float[] UpperBound { get; set; }
}

public class ForecastService
{
    private readonly MLContext _mlContext = new();

    // CACHE models per utility 
    private readonly Dictionary<string, ITransformer> _models = new();

    public float[] ForecastNextMonths(List<double> values, string key)
    {
        // Safety: not enough data
        if (values == null || values.Count < 12)
        {
            return Fallback(values);
        }

        var data = values.Select(v => new TimeSeriesData { Value = (float)v });
        var dataView = _mlContext.Data.LoadFromEnumerable(data);

        // Train ONCE per utility
        if (!_models.ContainsKey(key))
        {
            var pipeline = _mlContext.Forecasting.ForecastBySsa(
                outputColumnName: nameof(ForecastOutput.Forecasted),
                inputColumnName: nameof(TimeSeriesData.Value),

                windowSize: 12,                 // yearly seasonality
                seriesLength: values.Count,
                trainSize: values.Count,
                horizon: 3,

                isAdaptive: true,               // adapts to trend shifts
                confidenceLevel: 0.95f,
                confidenceLowerBoundColumn: nameof(ForecastOutput.LowerBound),
                confidenceUpperBoundColumn: nameof(ForecastOutput.UpperBound)
            );

            _models[key] = pipeline.Fit(dataView);
        }

        var engine = _models[key]
            .CreateTimeSeriesEngine<TimeSeriesData, ForecastOutput>(_mlContext);

        var forecast = engine.Predict();

        // final safety cleanup
        return CleanForecast(forecast.Forecasted, values);
    }

    //  FALLBACK 
    private float[] Fallback(List<double> values)
    {
        if (values == null || values.Count == 0)
            return new float[] { 0, 0, 0 };

        var avg = values.TakeLast(3).Average();

        return new float[]
        {
            (float)avg,
            (float)(avg * 1.02),
            (float)(avg * 1.03)
        };
    }

    // CLEAN OUTPUT
    private float[] CleanForecast(float[] forecast, List<double> history)
    {
        var last = history.Last();

        for (int i = 0; i < forecast.Length; i++)
        {
            // NaN / Infinity fix
            if (float.IsNaN(forecast[i]) || float.IsInfinity(forecast[i]))
                forecast[i] = (float)last;

            // Negative values fix
            if (forecast[i] < 0)
                forecast[i] = 0;

            // Unrealistic spike limiter 
            var maxAllowed = last * 2.5;
            var minAllowed = last * 0.4;

            forecast[i] = Math.Clamp(forecast[i], (float)minAllowed, (float)maxAllowed);

            last = forecast[i];
        }

        return forecast;
    }
}