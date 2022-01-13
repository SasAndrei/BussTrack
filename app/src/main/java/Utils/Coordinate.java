package Utils;

public class Coordinate {
    public double degrees;
    public double minutes;
    public double seconds;

    public Coordinate(double degrees, double minutes, double seconds)
    {
        this.degrees = degrees;
        this.minutes = minutes;
        this.seconds = seconds;
    }
    public Coordinate()
    {
    }

    public double asDouble()
    {
        return (Math.signum(degrees) * (Math.abs(degrees) + (minutes / 60.0) + (seconds / 3600.0)));
    }
}
