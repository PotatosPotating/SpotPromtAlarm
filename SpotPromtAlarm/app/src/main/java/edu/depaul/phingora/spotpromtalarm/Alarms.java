package edu.depaul.phingora.spotpromtalarm;


import java.util.ArrayList;

public class Alarms {


    private Alarms()
    {}
    private static ArrayList<SingleAlarm> alarms = new ArrayList<SingleAlarm>();
//    private static SingleAlarm[] alarms = new SingleAlarm[1000];
    private static int lastPosition=0;

    public static void addToList(SingleAlarm a)
    {
        alarms.add(a);
//        alarms[lastPosition]=a;
//        lastPosition++;
    }
    public static SingleAlarm getAlarm(int position)
    {
        return alarms.get(position);
    }
    public static int getLength()
    {
        return alarms.size();
    }
    public static SingleAlarm getAlarmByID(int id)
    {
        for(SingleAlarm s : alarms)
        {
            if(s.getid()==id)
                return s;
        }
        return null;
    }
    public static ArrayList<SingleAlarm> returnList() {
        return alarms;
    }

    public static void deleteByID(int idInt) {
        for(SingleAlarm s:alarms)
        {
            if(s.getid()==idInt)
                alarms.remove(s);
        }
    }
}
