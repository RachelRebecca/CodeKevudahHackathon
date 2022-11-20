import java.util.ArrayList;
import java.util.HashMap;

public class Calculator
{
    public int PTO;

    public int sickDays;

    public int mentalHealth;

    private HashMap<Integer, boolean[]> workingPaidDays;

    public Calculator(boolean leapYear, int pto, int sickDays, int mentalHealthDays,
                      ArrayList<Step2Panel> step2PanelArrayList, ArrayList<UnpaidHolidayPanel> unpaidHolidayPanelArrayList)
    {
        this.PTO = pto;
        this.sickDays = sickDays;
        this.mentalHealth = mentalHealthDays;

        workingPaidDays = new HashMap<>();
        for (int i = 0; i < (leapYear ? 366 : 365); i++)
        {
            workingPaidDays.put(i, new boolean[]{true, true});
        }

        ArrayList<Integer> workOff = new ArrayList<>();
        for (int i = 0; i < step2PanelArrayList.size(); i++)
        {
            int day = step2PanelArrayList.get(i).datePanel.getCorrespondingNumber();
            workOff.add(day);
        }

        //any work days already off with no loss of money
        for (int i = 0; i < workOff.size(); i++)
        {
            boolean[] dayInfo = workingPaidDays.get(workOff.get(i));
            dayInfo[0] = false;
            workingPaidDays.put(workOff.get(i), dayInfo);
        }

        calculateLostDays(unpaidHolidayPanelArrayList);
    }

    private int updateCalendar(int NumDaysOff, HashMap<Integer, boolean[]> workingPaidDays, int startDate, int lostDays)
    {
        while (NumDaysOff > 0)
        {
            boolean[] dayInfo = workingPaidDays.get(startDate);
            dayInfo[0] = false;
            workingPaidDays.put(startDate, dayInfo);

            if (PTO > 0)
            {
                PTO--;
            } else
            {
                if (mentalHealth > 0)
                {
                    mentalHealth--;
                } else
                {
                    if (sickDays > 0)
                    {
                        sickDays--;
                    } else
                    {
                        lostDays++;
                    }
                    dayInfo[1] = false;
                    workingPaidDays.put(startDate, dayInfo);
                }
            }

            NumDaysOff--;
            if (NumDaysOff > 0)
            {
                startDate++;
            }
        }
        return lostDays;
    }

    public int calculateLostDays(ArrayList<UnpaidHolidayPanel> unpaidHolidayPanelArrayList)
    {
        int lostDays = 0;

        for (int i = 0; i < unpaidHolidayPanelArrayList.size(); i++)
        {
            int day = unpaidHolidayPanelArrayList.get(i).datePanel.getCorrespondingNumber();
            int numDays = Integer.parseInt(unpaidHolidayPanelArrayList.get(i).daysToTakeOff.getText());
            lostDays = updateCalendar(numDays, workingPaidDays, day, lostDays);
        }

        return lostDays;
    }


}