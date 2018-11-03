package com.hariofspades.chatbot.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hariofspades.chatbot.MainActivity;
import com.hariofspades.chatbot.Pojo.ChatMessage;
import com.hariofspades.chatbot.R;

import java.util.List;

/**
 * Created by Hari on 11/05/16.
 */
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3;
    private Context mContext;


    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(context, R.layout.item_mine_message, data);
        this.mContext = context;
    }

    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);

        if (item.isMine() && !item.isImage()) return MY_MESSAGE;
        else if (!item.isMine() && !item.isImage()) return OTHER_MESSAGE;
        else if (item.isMine() && item.isImage()) return MY_IMAGE;
        else return OTHER_IMAGE;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());

        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        } else if (viewType == MY_IMAGE) {




            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tab_msg_layout, parent, false);


            TableLayout new_table = (TableLayout) convertView.findViewById(R.id.tab_test);
            String col = getItem(position).getCol_name();
            String[] row = getItem(position).getRow_data();


            TableRow tr1 = new TableRow(getContext());



            String [] rv= row;
            String [] cv = col.split(",") ;
            int rowCount = rv.length ;
            int columnCount = cv.length;



           TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
         //   TableLayout tableLayout = new TableLayout(this);
            new_table.setBackgroundColor(Color.BLACK);

            // 2) create tableRow params
            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
            tableRowParams.setMargins(1, 1, 1, 1);

            tableRowParams.weight = 1;




            for (int i = 0; i <= rowCount; i++) {
                // 3) create tableRow
                TableRow tableRow = new TableRow(getContext());
                tableRow.setBackgroundColor(Color.BLACK);

                for (int j= 0; j <=columnCount; j++) {
                    // 4) create textView
                    TextView textView = new TextView(getContext());
                    //  textView.setText(String.valueOf(j));
                    try{textView.setBackgroundColor(Color.parseColor("#FF00AA99"));}
                    catch(Exception e){textView.setBackgroundColor(Color.WHITE); }
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
                new_table.addView(tableRow, tableLayoutParams);
            }


            } else {
            //bkp



            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tab_msg_layout, parent, false);


            TableLayout new_table = (TableLayout) convertView.findViewById(R.id.tab_test);
            String col = getItem(position).getCol_name();
            String[] row = getItem(position).getRow_data();


            TableRow tr1 = new TableRow(getContext());
            tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            String[] col_data = col.split(",");


            try {
                for (int i = 0; i < col_data.length; i++) {
                    TextView textviewa = new TextView(getContext());
                    textviewa.setTextColor(Color.BLACK);
                    textviewa.setBackgroundColor(Color.WHITE);
                    try {
                        textviewa.setText(col_data[i]+"|");
                    } catch (Exception e) {
                        textviewa.setText(e.toString());
                    }
                    tr1.addView(textviewa);

                }
                new_table.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }catch(Exception e)
            {

                TextView textviewa = new TextView(getContext());
                textviewa.setTextColor(Color.BLACK);

                textviewa.setText(e.toString());
                tr1.addView(textviewa);
                new_table.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


            }


            try{

                for(int i=0;i<row.length;i++) {
                    String[] tempData = row[i].split(",");
                    for (int j = 0; j <= tempData.length; j++) {
                        TextView textviewa = new TextView(getContext());
                        textviewa.setTextColor(Color.BLACK);
                        textviewa.setText(tempData[i]);
                        tr1.addView(textviewa);
                    }
                    new_table.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }

            }catch (Exception e) {
                TextView textviewa = new TextView(getContext());
                textviewa.setTextColor(Color.BLACK);
                textviewa.setText(e.toString());
                tr1.addView(textviewa);
            }
        }

        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "onClick", Toast.LENGTH_LONG).show();
                if (mContext instanceof MainActivity) {
                    if (((MainActivity) mContext).step == 2) {
                        String abc = getItem(position).getContent();
                        ((MainActivity) mContext).selectQuery(abc);
                    }
                }
            }
        });


        return convertView;
    }
}
