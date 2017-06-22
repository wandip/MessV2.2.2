package com.messedup.saurabh.mess2.BackendLogic;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by saurabh on 23/4/17.
 */

public class Group {


    private String college;
    private String contact;
    private String email;
    private String name;
    private String qrcode;
    private HashMap<String,Integer> map;
    public static ArrayList<ArrayList<Object>> dip;
    static int grpnum;
    static boolean isDone;
/*    private int[][] a1=new int[100][100];
    private int[][] a2=new int[100][100];*/

   /* private Vector<Vector<Integer>> v1=new Vector<Vector<Integer>>();
    private Vector<Vector<Integer>> v2=new Vector<Vector<Integer>>();
*/

    private int[] sizearr;
    private static String[] uidarr;
    private Boolean flag4,flag5,flag6,flag7;
    private int SET_TYPE;
    public int cnt;




    public Group(String college, String contact, String email, String name, String qrcode) {
        this.college = college;
        this.contact = contact;
        this.email = email;
        this.name = name;
        this.qrcode = qrcode;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    Group()
    {

    }
   public Group(HashMap<String,Integer> map)
    {
        this.map=map;
        flag4=flag5=flag6=flag7=false;
    }

    public int getmapsize()
    {
        return map.size();
    }

    void initialiseArray()
    {
        sizearr=new int[getmapsize()];
        uidarr=new String[getmapsize()];
        int i=0;
        for (Map.Entry m:map.entrySet()) {
           sizearr[i]=(Integer)m.getValue();
            uidarr[i]=(String)m.getKey();
            i++;
        }

    }


    public void assignset()
    {
        initialiseArray();
        int N=getmapsize();

        dip = new ArrayList<>();
        isDone=false;
        dip.clear();
        if(isKPartitionPossible(sizearr,N,4))
        {
            SET_TYPE=4;

        }
        else if(isKPartitionPossible(sizearr,N,7))
        {
            SET_TYPE=7;
        }
        else if(isKPartitionPossible(sizearr,N,5))
        {
            SET_TYPE=5;
        }
        else if(isKPartitionPossible(sizearr,N,9))
        {
            SET_TYPE=9;
        }
        else if(isKPartitionPossible(sizearr,N,6))
        {
            SET_TYPE=6;
        }
        else
        {
            SET_TYPE=-1;
        }

        if(SET_TYPE!=-1)
        {
            cnt=0;
            for (ArrayList<Object> temp:dip
                    ) {
                if(temp.size()!=0)
                    cnt++;

            }
        }
        Log.v("E_VALUE","SET COUNT : "+SET_TYPE+"Actual count :"+cnt);
        Log.v("E_VALUE","ARRAYLIST: "+dip);
        AssignMessLogic assignMessLogic= new AssignMessLogic();
        assignMessLogic.getMessInfo();

    }


    private static boolean isKPartitionPossible(int arr[], int N, int K)
    {

        isDone=false;
        dip.clear();
        int sum = 0;
        for (int i = 0; i < N; i++)
            sum += arr[i];
        if (sum % K != 0)
            return false;

        //  the sum of each subset should be subset (= sum / K)
        int subset = sum / K;
        int[] subsetSum = new int[K];
        boolean[] taken = new boolean[N];

        //  Initialize sum of each subset from 0
        for (int i = 0; i < K; i++)
            subsetSum[i] = 0;

        //  mark all elements as not taken
        for (int i = 0; i < N; i++)
            taken[i] = false;

        // initialize first subsubset sum as last element of
        // array and mark that as taken
        subsetSum[0] = arr[N - 1];
        taken[N - 1] = true;

        //System.out.print("index 0 " +arr[N-1]+" "+(N-1));
        dip.add(0,new ArrayList<>());
        dip.get(0).add(uidarr[N-1]);

        if (subset < subsetSum[0])
            return false;

        //  call recursive method to check K-subsetition condition
        return isKPartitionPossibleRec(arr, subsetSum, taken,
                subset, K, N, 0, N - 1);
    }

    private static boolean isKPartitionPossibleRec(int[] arr, int[] subsetSum, boolean[] taken, int subset, int K, int N, int curIdx, int limitIdx) {
        if (subsetSum[curIdx] == subset)
        {
        /*  current index (K - 2) represents (K - 1) subsets of equal
            sum last subsetition will already remain with sum 'subset'*/
            if (curIdx == K - 1) {
                //System.out.println("Last subset done");
                //System.out.println("Clear at index : "+(K-1)+"and add");
                isDone=true;
                return true;
            }
            //  recursive call for next subsetition
            //System.out.println("Clear at index : "+curIdx+" and add");
            return isKPartitionPossibleRec(arr, subsetSum, taken, subset,
                    K, N, curIdx + 1, N - 1);
        }

        //  start from limitIdx and include elements into current subsetition
        for (int i = limitIdx; i >= 0; i--)
        {
            //  if already taken, continue
            if (taken[i])
                continue;
            int tmp = subsetSum[curIdx] + arr[i];

            // if temp is less than subset then only include the element
            // and call recursively
            if (tmp <= subset)
            {
                //  mark the element and include into current subsetition sum
                taken[i] = true;
                subsetSum[curIdx] += arr[i];

                //System.out.println("index "+curIdx+" + "+arr[i]+" "+ i);
                dip.add(new ArrayList<>());
                dip.get(curIdx).add(uidarr[i]);

                boolean nxt = isKPartitionPossibleRec(arr, subsetSum, taken,
                        subset, K, N, curIdx, i - 1);

                // after recursive call unmark the element and remove from
                // subsetition sum
                taken[i] = false;
                subsetSum[curIdx] -= arr[i];
                if(!isDone) {
                    // System.out.println("index "+curIdx+" - " + arr[i] + " " + i);
                    dip.get(curIdx).remove(dip.get(curIdx).size()-1);
                }if (nxt)
                return true;
            }
        }
        return false;
    }




}
