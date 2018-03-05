package edu.depaul.phingora.spotpromtalarm;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class SingleAlarm{
    private Location location=new Location("");
    private int id;
    private String name="default";
    private String description="default";
    public SingleAlarm(LatLng l,int id,String name,String description)
    {
        location.setLatitude(l.latitude);
        location.setLongitude(l.longitude);
        this.id=id;
        this.name=name;
        this.description=description;
    }
    public Location getLocation()
    {
        return location;
    }
    public int getid()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public String getDescription()
    {
        return description;
    }
    public void setLocation(LatLng l)
    {
        location.setLatitude(l.latitude);
        location.setLongitude(l.longitude);
    }
    public void setId(int i)
    {
        id=i;
    }
    public void setName(String n)
    {
        name = n;
    }
    public void setDescription(String d)
    {
        description = d;
    }
}
