package org.firstinspires.ftc.teamcode;

public class DetectionStorage {
    public double x;
    public double y;
    public double z;
    public int id;
    public String name;
    public double distance;
    public double distance2D;
    public int camId;

    public DetectionStorage(double inx, double iny, double inz, int inid, String inname){
        x = inx;
        y = iny;
        z = inz;
        id = inid;
        name = inname;
        distance = Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
        distance2D = Math.hypot(x , y);
    }
    public DetectionStorage(double inx, double iny, double inz, int inid, String inname, int incamId){
        x = inx;
        y = iny;
        z = inz;
        id = inid;
        name = inname;
        camId = incamId;
        distance = Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
        distance2D = Math.hypot(x , y);
    }
    public DetectionStorage(double inx, double iny, String inname){
        x = inx;
        y = iny;
        name = inname;
    }
    public double getDistance(){
        double out = Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
        return out;
    }

    public String toString(){
        return "X: " + Math.round(x) + " Y: " + Math.round(y) + " Z: " + Math.round(z) + "CamId: " + camId;
    }
}
