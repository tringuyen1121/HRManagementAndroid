package com.tringuyen.example.hrmanagerandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class InfoListActivity extends AppCompatActivity {

    public int viewType = 0;

    public List<Employee> arrEmployee = MainActivity.arrEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(MainActivity.RESULT_OK);
                finish();
            }
        });

        TextView toolbar_info_list_title = (TextView) findViewById(R.id.toolbar_info_list_title);

        viewType = getIntent().getIntExtra("viewType", 0);
        if(viewType==1){
            toolbar_info_list_title.setText("View Employees' Age/DOB");
            RelativeLayout rlTotalPayroll = (RelativeLayout) findViewById(R.id.rlTotalPayroll);
            rlTotalPayroll.setVisibility(View.GONE);
        } else {
            toolbar_info_list_title.setText("Calculation Payroll");
            double totalPR = 0;
            for (Employee employee : arrEmployee) {
                totalPR = totalPR + (double) employee.calcEaringings();
            }

            NumberFormat format = NumberFormat.getCurrencyInstance();
            TextView txtTotalPayroll = (TextView) findViewById(R.id.txtTotalPayroll);
            txtTotalPayroll.setText("Total Payroll : " + format.format(totalPR));
        }

        InfoListAdapter listAdapter = new InfoListAdapter(this,android.R.layout.simple_list_item_1,arrEmployee);
        ListView lvInfoList = (ListView) findViewById(R.id.lvInfoList);
        lvInfoList.setAdapter(listAdapter);

    }

    public class InfoListAdapter extends ArrayAdapter<Employee> {
        public List<Employee> arrEmployee = null;

        public InfoListAdapter(Context context, int resource, List<Employee> list) {
            super(context, resource, list);
            this.arrEmployee = list;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView txt = (TextView) convertView.findViewById(android.R.id.text1);
            if (viewType==0){
                NumberFormat format = NumberFormat.getCurrencyInstance();
                txt.setText("Name: " + arrEmployee.get(position).getName() +
                        "\nPayroll: " + format.format(arrEmployee.get(position).calcEaringings()));
            } else {
                txt.setText("Name: " + arrEmployee.get(position).getName() +
                        "\nAge: " + String.valueOf(arrEmployee.get(position).getAge()) +
                        ", DOB Year: " + String.valueOf(arrEmployee.get(position).calcBirthYear())) ;
            }
            return convertView;
        }
    }

}
