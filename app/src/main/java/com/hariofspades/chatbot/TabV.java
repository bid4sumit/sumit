package com.hariofspades.chatbot;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by sumit.bx.kumar on 12-09-2017.
 */

public class TabV extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String colnames[] = null;
        ArrayList<String> rowData =null;

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            colnames = (String[]) bd.get("colnames");
            rowData = (ArrayList<String>)bd.get("rowdata");
        }


       // String[] row = { "ROW1", "ROW2", "Row3", "Row4", "Row 5", "Row 6", "Row 7" };
       String[] row= new String[rowData.size()];
       rowData.toArray(row);
       // String[] row =  {"a,b","c,d"};
        String[] column = colnames;
                //{ "COLUMN1", "COLUMN2", "COLUMN3", "COLUMN4",
                //"COLUMN5", "COLUMN6" };
        int rl=row.length; int cl=column.length;


        ScrollView sv = new ScrollView(this);
        TableLayout tableLayout = createTableLayout(row, column,rl, cl);
        HorizontalScrollView hsv = new HorizontalScrollView(this);

        hsv.addView(tableLayout);
        sv.addView(hsv);
        setContentView(sv);

    }

    public void makeCellEmpty(TableLayout tableLayout, int rowIndex, int columnIndex) {
        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);

        // get cell from row with columnIndex
        TextView textView = (TextView)tableRow.getChildAt(columnIndex);

        // make it black
        textView.setBackgroundColor(Color.BLACK);
    }
    public void setHeaderTitle(TableLayout tableLayout, int rowIndex, int columnIndex){

        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);

        // get cell from row with columnIndex
        TextView textView = (TextView)tableRow.getChildAt(columnIndex);

        textView.setText("Hello");
    }

    private TableLayout createTableLayout(String [] rv, String [] cv,int rowCount, int columnCount) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);

        tableRowParams.weight = 1;

        for (int i = 0; i <= rowCount; i++) {
            // 3) create tableRow
            TableRow tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.BLACK);

            for (int j= 0; j <=columnCount; j++) {
                // 4) create textView
                TextView textView = new TextView(this);
                //  textView.setText(String.valueOf(j));
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                String s1 = Integer.toString(i);
                String s2 = Integer.toString(j);
                String s3 = s1 + s2;
                int id = Integer.parseInt(s3);

                if (i ==0 && j==0){
                    textView.setText(" S.No ");
                } else if(i==0){

                    textView.setText(cv[j-1]);
                }else if( j==0){

                    textView.setText(""+(i));
                }else {
                    //textView.setText(""+id);
                    try{
                    String dd = rv[i-1];
                    String temp[] = dd.split(",");
                    textView.setText(temp[j-1]);}
                    catch(Exception e)
                    {
                        textView.setText(""+id+e.toString());
                    }
                    // check id=23

                }

                // 5) add textView to tableRow
                tableRow.addView(textView, tableRowParams);
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow, tableLayoutParams);
        }

        return tableLayout;
    }
}
