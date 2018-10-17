package com.net2plan.gui.plugins.networkDesign.openStack.extra;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;

import java.util.List;

public class OpenStackSummary  extends OpenStackNetworkElement
{

    private double med;
    private double min;
    private double max;
    private double desv;
    private double mod;
    private double median;
    private String id;

    private double[] values;

    public static OpenStackSummary createFromAddSummary (OpenStackNet osn ,String id,double[] values, OpenStackClient openStackClient)
    {
        final OpenStackSummary res = new OpenStackSummary(osn,values,openStackClient);
        res.id = id+"m";
        res.med = media(values);
        res.min = minimo(values);
        res.max = maximo(values);
        res.mod = moda(values);
        res.desv = desviacion(values);
        res.median = mediana(values);
        return res;
    }

    private OpenStackSummary (OpenStackNet osn, double[] values , OpenStackClient openStackClient)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackSummaries,openStackClient);
        this.values = values;
    }

    @Override
    public String getId () { return this.id; }
    public double getMed () { return this.med; }
    public double getMin () { return this.min; }
    public double getMax () { return this.max; }
    public double getDesv () { return this.desv; }
    public double getModa () { return this.mod; }
    public double getMediana () { return this.median; }

    public static double media(double [] values){

        double prom = 0.0;
        for ( int i = 0; i < values.length; i++ )
            prom += values[i];

        return prom / ( double ) values.length;

    }
    public static double minimo(double [] values){


        double minimo = values[0]; // Declaramos e inicializamos el máximo.

        for (int i = 0; i < values.length; i++){

            if (minimo > values[i])
                minimo = values[i];
        }
        return minimo;
    }
    public static double maximo(double [] values){

        double maximo = values[0]; // Declaramos e inicializamos el máximo.

        for (int i = 0; i < values.length; i++){
            if (maximo < values[i])
                maximo = values[i];
        }
        return maximo;
    }
    public static double desviacion(double [] values){

        double prom, sum = 0; int i, n = values.length;
        prom = media ( values );

        for ( i = 0; i < n; i++ )
            sum += Math.pow ( values [ i ] - prom, 2 );

        return Math.sqrt ( sum / ( double ) n );
    }
    public static double moda(double [] values){

        int i, j, moda = 0, n = values.length, frec;
        int frecTemp, frecModa = 0;
        double moda1 = -1;

        // ordenar de menor a mayor
        values = orderArray ( values, 0 );

        for ( i = 0; i < n; i++ ) {
            frecTemp = 1;
            for ( j = i + 1; j < n; j++ ) {
                if ( values [ i ] == values [ j ] )
                    frecTemp++;
            }
            if ( frecTemp > frecModa ) {
                frecModa = frecTemp;
                moda1 = values[ i ];
            }
        }
        return moda1;
    }
    public static double mediana ( double [ ] values ) {
        int pos = 0, n = values.length;
        double temp = 0, temp0 = 0;
        // ordenar de menor a mayor
        values = orderArray ( values, 0 );

        temp = n / 2;
        if ( n % 2 == 0 ) {
            pos = (int)temp;
            temp0 = (double)(values [ pos ] / values [ pos + 1 ]);
        }
        if ( n % 2 == 1 ) {
            pos = (int)(temp + 0.5);
            temp0 = (double)(values [ pos ]);
        }

        return temp0;
    }

    public static double [ ] orderArray ( double [ ] v, int ord ) {
        int i, j, n = v.length;
        double aux = 0;

        for ( i = 0; i < n - 1; i++ )
            for ( j = i + 1; j < n; j++ )
                if ( ord == 0 )
                    if ( v [ i ] > v [ j ] ) {
                        aux = v [ j ];
                        v [ j ] = v [ i ];
                        v [ i ] = aux;
                    }
                    else if ( ord == 1 )
                        if ( v [ i ] < v [ j ] ) {
                            aux = v [ i ];
                            v [ i ] = v [ j ];
                            v [ j ] = aux;
                        }

        return v;
    }

    @Override
    public String get50CharactersDescription()
    {

        return "";
    }


}