package com.tringuyen.example.hrmanagerandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListActivity extends AppCompatActivity {

    public boolean isEdit = false;

    public List<Employee> arrEmployee = MainActivity.arrEmployee;
    SearchView empSearchView = null;
    ListView empListView = null;
    EmployeeListAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
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

        empSearchView = (SearchView) findViewById(R.id.empSearchView);
        empListView = (ListView) findViewById(R.id.empListView);
        mAdapter = new EmployeeListAdapter(this, R.layout.employeelist_item, arrEmployee);
        empListView.setAdapter(mAdapter);
        empListView.setTextFilterEnabled(true);

        empSearchView.setVisibility(View.VISIBLE);
        empSearchView.setIconifiedByDefault(false);
        empSearchView.setSubmitButtonEnabled(true);
        empSearchView.setQueryHint("Input Name");
        empSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query.equals("")) {
                    mAdapter.reset();
                } else {
                    mAdapter.filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query == null || query.equals("")) {
                    mAdapter.reset();
                } else {
                    mAdapter.filter(query);
                }
                return false;
            }
        });

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        String searchValue = getIntent().getStringExtra("searchValue");
        if (isEdit && searchValue != null && !searchValue.equals("")) {
            empSearchView.setQuery(searchValue, false);
            //mAdapter.filter(searchValue);
        }
    }

    public class EmployeeListAdapter extends ArrayAdapter<Employee>  {
        public ArrayList<Integer> mEmpListIndex = null;
        public List<Employee> mEmpList = null;
        public Boolean isSearch = false;

        public EmployeeListAdapter(Context context, int resource, List<Employee> list) {
            super(context,resource);
            mEmpList = list;
            mEmpListIndex = new ArrayList<Integer>();
        }

        @Override
        public int getCount() {
            if( isSearch ){
                return mEmpListIndex.size();
            }
            return mEmpList.size();
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.employeelist_item, parent, false);
            }

            TextView tvNameAge = (TextView) convertView.findViewById(R.id.tvNameAge);
            TextView tvDOBCountry = (TextView) convertView.findViewById(R.id.tvDOBCountry);
            TextView tvPayroll = (TextView) convertView.findViewById(R.id.tvPayroll);
            TextView tvVehicle = (TextView) convertView.findViewById(R.id.tvVehicle);

            int index = -1;
            if(isSearch){
                index = mEmpListIndex.get(position);
            } else {
                index = position;
            }

            Employee employee = mEmpList.get(index);

            tvNameAge.setText("Name : "+employee.getName()+", Age : "+employee.getAge());
            tvDOBCountry.setText("DOB : "+employee.getDOB()+", Country : "+employee.getCountry());
            if (employee instanceof FullTime) {
                tvPayroll.setText("Salary : "+((FullTime) employee).salary+", Bonus : "+((FullTime) employee).bonus);
            } else if (employee instanceof PartTime) {
                tvPayroll.setText("Hours : "+((PartTime) employee).hoursWorked+", Rate : "+((PartTime) employee).rate);
            } else if (employee instanceof Intern) {
                tvPayroll.setText("School : "+((Intern) employee).collegeName);
            }

            if (employee.vehicle_owned != null) {
                tvVehicle.setText("Vehicle Plate: "+employee.vehicle_owned.plateNumber+", Make : "+employee.vehicle_owned.make);
            } else {
                tvVehicle.setText("No Vehicle Info");
            }

            Button btnEditEmpItem = (Button) convertView.findViewById(R.id.btnEditEmpItem);
            btnEditEmpItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = -1;
                    if(isSearch){
                        index = mEmpListIndex.get(position);
                    } else {
                        index = position;
                    }

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("isEdit",true);
                    intent.putExtra("position",index);
                    setResult(MainActivity.RESULT_EDIT,intent);
                    finish();
                }
            });

            Button btnDeleteEmpItem = (Button) convertView.findViewById(R.id.btnDeleteEmpItem);
            btnDeleteEmpItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = -1;
                    if(isSearch){
                        index = mEmpListIndex.get(position);
                    } else {
                        index = position;
                    }
                    arrEmployee.remove(index);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        public void reset() {
            mEmpListIndex.clear();
            isSearch = false;
            notifyDataSetChanged();
        }

        public void filter(String searchText) {
            mEmpListIndex.clear();
            for (int i = 0; i< mEmpList.size(); i++){
                if (mEmpList.get(i).getName().toLowerCase().contains(searchText.toLowerCase()))
                    mEmpListIndex.add(i);
            }
            isSearch = true;
            notifyDataSetChanged();
        }
    }

}

