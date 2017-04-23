package com.example.saurabh.mess2.BackendLogic;

import android.util.Log;

import com.example.saurabh.mess2.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

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
    private int count1,count2;
    private Stack<Integer> s=new Stack<>();
    private ArrayList<ArrayList<Integer>>a1 =new ArrayList<ArrayList<Integer>>(100);
    private ArrayList<ArrayList<Integer>>a2 =new ArrayList<ArrayList<Integer>>(100);

/*    private int[][] a1=new int[100][100];
    private int[][] a2=new int[100][100];*/

   /* private Vector<Vector<Integer>> v1=new Vector<Vector<Integer>>();
    private Vector<Vector<Integer>> v2=new Vector<Vector<Integer>>();
*/

    private int[] sizearr;
    private String[] uidarr;
    private Boolean flag4,flag5,flag6,flag7;
    private int SET_TYPE;




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

        if(isPartitionPossible(sizearr,N,4))
        {
            SET_TYPE=4;

        }
        else if(isPartitionPossible(sizearr,N,7))
        {
            SET_TYPE=7;
        }
        else if(isPartitionPossible(sizearr,N,5))
        {
            SET_TYPE=5;
        }
        else if(isPartitionPossible(sizearr,N,6))
        {
            SET_TYPE=6;
        }
        else
        {
            SET_TYPE=-1;
        }

        Log.v("E_VALUE","SET COUNT : "+SET_TYPE);

    }


    private boolean isPartitionPossible(int[] sizearr, int N, int K) {

        if(N<K) //Hatai condition
            return false;
        int sum=0;
        for(int i=0;i<N;i++)
        {
            sum+=sizearr[i];

        }
        if(sum%K!=0)
        {
            return false;
        }
        int subset = sum/K;
        int[] subsetsum=new int[K];
        boolean[] taken=new boolean[N];

        for(int i=0;i<K;i++)
        {
            subsetsum[i]=0;
        }
        for(int i=0;i<N;i++)
        {
            taken[i]=false;
        }

        subsetsum[0]=sizearr[N-1];
        s.push(sizearr[N-1]);
        taken[N-1]=true;

        if(subset<subsetsum[0])
        {
            return false;
        }

        return isPartitionPossibleRec(sizearr,subsetsum,taken,subset,K,N,0,N-1);


    }

    private boolean isPartitionPossibleRec(int[] sizearr, int[] subsetsum, boolean[] taken, int subset, int K, int N, int curIdx, int limitIdx) {

        if(subsetsum[curIdx]==subset)
        {

            if(curIdx==K-1)
            {
                for(int i=0;i<count1;i++)
                {
                    for(int j=0;j<a1.get(i).size();j++)
                    {
                        a2.get(i).add(a1.get(i).get(j));


                    }
                }

                count2=count1;
                count1=0;
                for(int i=0;i<count1;i++) {
                    for(int j=0;j<a1.get(i).size();j++)
                     a1.get(i).set(j,0);
                }

                return true;

            }
            while(!s.isEmpty())
            {
                a1.get(count1).add(s.peek());
                s.pop();

            }
            count1++;

            return isPartitionPossibleRec(sizearr,subsetsum,taken,subset,K,N,curIdx+1,N-1);



        }

        for(int i=limitIdx;i>=0;i--)
        {
            if (taken[i])
            {
                continue;
            }

            int temp=subsetsum[curIdx]+sizearr[i];

            if(temp<=subset)
            {
                taken[i]=true;
                subsetsum[curIdx]+=sizearr[i];
                s.push(sizearr[i]);

                boolean next=isPartitionPossibleRec(sizearr,subsetsum,taken,subset,K,N,curIdx,i-1);

                taken[i]=false;

                subsetsum[curIdx]-=sizearr[i];

                if(s.isEmpty())
                {
                    count1--;
                    a1.get(count1).remove(a1.get(count1).size()-1);

                    if(a1.get(count1).isEmpty())
                    {
                        count1--;
                    }

                    count1++;
                }
                else
                {
                    s.pop();
                }
                if(next)
                {
                    return true;
                }
            }
        }

        return false;
    }




}
