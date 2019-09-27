package com.tringuyen.example.hrmanagerandroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static boolean isLoad = false;
    public boolean isEdit = false;

    public static final int GOTO_EMPLOYEELIST = 0;
    public static final int GOTO_INFOLIST = 1;
    public static final int RESULT_EDIT = 10;

    public static List<Employee> arrEmployee = new ArrayList<Employee>();
    public static Employee currentEmployee = null;

    private String[] arrCountries = null;

    public EditText txtName = null;
    public EditText txtAge = null;
    public EditText txtDOB = null;
    public EditText txtCountry = null;
    public EditText txtSalaryHoursSchool = null;
    public EditText txtBonusRate = null;
    public EditText txtPlate = null;
    public EditText txtMake = null;

    public TextView salaryhoursschoolTextView = null;
    public TextView bonusrateTextView = null;

    RadioGroup radioEmployeeGroup = null;
    public Button btnAddChange = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        if (!isLoad)
            loadSharedPreferences(MainActivity.this);

        // Get Country List
        Resources res = getResources();
        arrCountries = res.getStringArray(R.array.country_list);

        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        txtDOB = findViewById(R.id.txtDOB);
        txtCountry = findViewById(R.id.txtCountry);
        txtSalaryHoursSchool = findViewById(R.id.txtSalaryHoursSchool);
        txtBonusRate = findViewById(R.id.txtBonusRate);
        txtPlate = findViewById(R.id.txtPlate);
        txtMake =  findViewById(R.id.txtMake);

        salaryhoursschoolTextView = findViewById(R.id.salaryhoursschoolTextView);
        bonusrateTextView = findViewById(R.id.bonusrateTextView);

        radioEmployeeGroup = this.findViewById(R.id.radioEmployeeGroup);
        radioEmployeeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (isEdit) {
                    clearField(false);
                    changeEditState(false);
                }
                currentEmployee = null;
                switch (i) {
                    case R.id.radioFullTime:
                        salaryhoursschoolTextView.setText("Salary");
                        txtSalaryHoursSchool.setInputType(InputType.TYPE_CLASS_NUMBER);
                        bonusrateTextView.setText("Bonus");
                        bonusrateTextView.setVisibility(View.VISIBLE);
                        txtBonusRate.setVisibility(View.VISIBLE);
                        txtSalaryHoursSchool.setHint("Salary");
                        txtBonusRate.setHint("Bonus");
                        break;
                    case R.id.radioPartTime:
                        salaryhoursschoolTextView.setText("Hours");
                        txtSalaryHoursSchool.setInputType(InputType.TYPE_CLASS_NUMBER);
                        bonusrateTextView.setText("Rate");
                        bonusrateTextView.setVisibility(View.VISIBLE);
                        txtBonusRate.setVisibility(View.VISIBLE);
                        txtSalaryHoursSchool.setHint("Hours");
                        txtBonusRate.setHint("Rate");
                        break;
                    case R.id.radioIntern:
                        salaryhoursschoolTextView.setText("School");
                        txtSalaryHoursSchool.setInputType(InputType.TYPE_CLASS_TEXT);
                        bonusrateTextView.setVisibility(View.GONE);
                        txtBonusRate.setVisibility(View.GONE);
                        txtSalaryHoursSchool.setHint("University or College");
                        break;
                }
            }
        });

        Button btnDatePicker = (Button) findViewById(R.id.btnDOBPicker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                StringBuilder selDate = new StringBuilder();
                                selDate.append(year).append("-");
                                if (monthOfYear < 10)
                                    selDate.append("0").append(monthOfYear + 1).append("-");
                                else
                                    selDate.append(monthOfYear + 1).append("-");
                                if (dayOfMonth < 10)
                                    selDate.append("0").append(dayOfMonth);
                                else
                                    selDate.append(dayOfMonth);
                                //Log.d(TAG,"Selected Date :" + selDate.toString());
                                txtDOB.setText(selDate);
                            }
                        },
                        year, month, day);
                dialog.setTitle("Choose Date Of Birth");
                dialog.show();
            }
        });

        Button btnCountryPicker = (Button) findViewById(R.id.btnCountryPicker);
        btnCountryPicker.setOnClickListener(new View.OnClickListener() {
            public int curCountryIndex = -1;
            public int offsetIndex = 2;
            public int selCountryIndex = -1;

            @Override
            public void onClick(View view) {
                View outerView = LayoutInflater.from(MainActivity.this).inflate(R.layout.pickerview_dialog , null);
                PickerView pvd = outerView.findViewById(R.id.picker_view);
                pvd.setOffset(offsetIndex);
                pvd.setItems(Arrays.asList(arrCountries));
                if (selCountryIndex > -1) pvd.setSeletion(selCountryIndex);
                else pvd.setSeletion(0);
                pvd.setOnPickerViewDialogListener(new PickerView.OnPickerViewDialogListener() {
                    @Override
                    public void onSelected(int selectedIndex, String country) {
                        curCountryIndex = (selectedIndex - offsetIndex);
                        Log.d(TAG, "selectedIndex:" + String.valueOf(selectedIndex));
                    }
                });

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Choose Country");
                dialog.setView(outerView);
                dialog.setNegativeButton("Cancel", null);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (curCountryIndex < 0) selCountryIndex = 0;   // no selected
                        else selCountryIndex = curCountryIndex;
                        //Log.d(TAG,"selCountryIndex:"+String.valueOf(selCountryIndex));
                        txtCountry.setText(arrCountries[selCountryIndex]);
                    }
                });
                dialog.show();
            }
        });

        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                clearField(false);
                changeEditState(false);
            }
        });

        btnAddChange = findViewById(R.id.btnAddChange);
        btnAddChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "";
                if (isEdit) {
                    if (currentEmployee instanceof FullTime) {
                        if(!isValidRecord(0)) return;

                        FullTime empRef = (FullTime) currentEmployee;
                        empRef.setName(txtName.getText().toString());
                        empRef.setAge(Integer.parseInt(txtAge.getText().toString()));
                        empRef.setDOB(txtDOB.getText().toString());
                        empRef.setCountry(txtCountry.getText().toString());
                        empRef.salary = Integer.parseInt(txtSalaryHoursSchool.getText().toString());
                        empRef.bonus = Integer.parseInt(txtBonusRate.getText().toString());

                        msg = "FullTime Employee Record is changed";
                    } else if (currentEmployee instanceof PartTime) {
                        if(!isValidRecord(1)) return;

                        PartTime empRef = (PartTime) currentEmployee;
                        empRef.setName(txtName.getText().toString());
                        empRef.setAge(Integer.parseInt(txtAge.getText().toString()));
                        empRef.setDOB(txtDOB.getText().toString());
                        empRef.setCountry(txtCountry.getText().toString());
                        empRef.hoursWorked = Integer.parseInt(txtSalaryHoursSchool.getText().toString());
                        empRef.rate = Integer.parseInt(txtBonusRate.getText().toString());

                        msg = "FullTime Employee Record is changed";
                    } else if (currentEmployee instanceof Intern) {
                        if(!isValidRecord(2)) return;

                        Intern empRef = (Intern) currentEmployee;
                        empRef.setName(txtName.getText().toString());
                        empRef.setAge(Integer.parseInt(txtAge.getText().toString()));
                        empRef.setDOB(txtDOB.getText().toString());
                        empRef.setCountry(txtCountry.getText().toString());
                        empRef.collegeName =txtSalaryHoursSchool.getText().toString();

                        msg = "FullTime Employee Record is changed";
                    }

                    if (currentEmployee.vehicle_owned != null) {
                        currentEmployee.vehicle_owned.plateNumber = txtPlate.getText().toString();
                        currentEmployee.vehicle_owned.make = txtMake.getText().toString();
                    }
                } else {
                    Vehicle v1 = null;
                    if ((txtPlate.getText().toString()!= null && !txtPlate.getText().toString().equals(""))
                            && (txtMake.getText().toString()!=null && !txtMake.getText().toString().equals(""))){
                        v1 = new Vehicle(txtPlate.getText().toString(), txtMake.getText().toString());
                    }
                    if (radioEmployeeGroup.getCheckedRadioButtonId() == R.id.radioFullTime){
                        // FullTime
                        if(!isValidRecord(0)) return;

                        FullTime ft = new FullTime(txtName.getText().toString(),
                                Integer.parseInt(txtAge.getText().toString()),
                                txtDOB.getText().toString(),
                                txtCountry.getText().toString(),
                                Integer.parseInt(txtSalaryHoursSchool.getText().toString()),
                                Integer.parseInt(txtBonusRate.getText().toString()),
                                v1);

                        arrEmployee.add(ft);
                        currentEmployee = ft;
                        changeEditState(true);

                        msg = "FullTime Employee Record is added";
                    } else if (radioEmployeeGroup.getCheckedRadioButtonId() == R.id.radioPartTime){
                        // PartTime
                        if(!isValidRecord(1)) return;

                        PartTime pt = new PartTime(txtName.getText().toString(),
                                Integer.parseInt(txtAge.getText().toString()),
                                txtDOB.getText().toString(),
                                txtCountry.getText().toString(),
                                Integer.parseInt(txtSalaryHoursSchool.getText().toString()),
                                Integer.parseInt(txtBonusRate.getText().toString()),
                                v1);

                        arrEmployee.add(pt);
                        currentEmployee = pt;
                        changeEditState(true);

                        msg = "PartTime Employee Record is added";
                    } else if (radioEmployeeGroup.getCheckedRadioButtonId() == R.id.radioIntern){
                        // Intern
                        if(!isValidRecord(2)) return;

                        Intern it = new Intern(txtName.getText().toString(),
                                Integer.parseInt(txtAge.getText().toString()),
                                txtDOB.getText().toString(),
                                txtCountry.getText().toString(),
                                txtSalaryHoursSchool.getText().toString(),
                                v1);

                        arrEmployee.add(it);
                        currentEmployee = it;
                        changeEditState(true);

                        msg = "Intern Employee Record is added";
                    }
                }
                if(!msg.equals(""))
                    alertMessage(MainActivity.this, "Alert", msg);
            }
        });

        // calculate payroll button
        Button btnCalcPayroll = (Button) findViewById(R.id.btnCalcPayroll);
        btnCalcPayroll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InfoListActivity.class);
                intent.putExtra("viewType",0);
                startActivityForResult(intent, GOTO_INFOLIST);
            }
        });

        // calculate age button
        Button btnCalcAge = (Button) findViewById(R.id.btnCalcAge);
        btnCalcAge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, InfoListActivity.class);
//                intent.putExtra("viewType",1);
//                startActivityForResult(intent,  GOTO_INFOLIST);
                if(txtDOB.getText().toString().equals("")) {
                    alertMessage(MainActivity.this,"Alert", "Please fill Employee's Date of Birth ");
                    txtDOB.findFocus();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                Integer currentYear = calendar.get(Calendar.YEAR);
                String[] dateArr = txtDOB.getText().toString().split("-");
                Integer birthYear = Integer.parseInt(dateArr[0]);
                Integer age = currentYear - birthYear;
                txtAge.setText(age.toString());
            }
        });

        // search btn
        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmployeeListActivity.class);
                intent.putExtra("isEdit",true);
                intent.putExtra("searchValue",txtName.getText().toString());
                startActivityForResult(intent,  GOTO_EMPLOYEELIST);
            }
        });

        // list
        Button btnEmpList = findViewById(R.id.btnEmpList);
        btnEmpList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmployeeListActivity.class);
                startActivityForResult(intent, GOTO_EMPLOYEELIST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOTO_EMPLOYEELIST) {
            if (resultCode == RESULT_EDIT) {
                // Check edit state
                isEdit = data.getBooleanExtra("isEdit",false);
                if (isEdit) {
                    int edit_pos = data.getIntExtra("position",-1);
                    if (edit_pos > -1) {
                        display(arrEmployee.get(edit_pos));
                        currentEmployee = arrEmployee.get(edit_pos);
                        changeEditState(true);
                    } else {
                        changeEditState(false);
                    }
                }
                else {
                    changeEditState(false);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        saveSharedPreferences(MainActivity.this);
        super.onDestroy();
    }

    private Boolean isValidRecord(Integer employeeType){
        Boolean isValid = true;
        String msg = "";

        if(txtName.getText().toString().equals("")) {
            msg = "Please fill Employee's Name ";
            txtName.findFocus();
            isValid = false;
        }
        else if(txtAge.getText().toString().equals("")) {
            msg = "Please fill Employee's Age ";
            txtAge.findFocus();
            isValid = false;
        }
        else if(txtDOB.getText().toString().equals("")) {
            msg = "Please fill Employee's Date of Birth ";
            txtDOB.findFocus();
            isValid = false;
        }
        else if(txtCountry.getText().toString().equals("")) {
            msg = "Please fill Employee's Country ";
            txtCountry.findFocus();
            isValid = false;
        }
        else if(txtSalaryHoursSchool.getText().toString().equals("")) {
            if(employeeType==1)
                msg = "Please fill Employee's Hours ";
            else if(employeeType==2)
                msg = "Please fill Employee's School ";
            else
                msg = "Please fill Employee's Salary ";
            txtSalaryHoursSchool.findFocus();
            isValid = false;
        }
        else if(employeeType!=2 && txtBonusRate.getText().toString().equals("")) {
            if(employeeType==1)
                msg = "Please fill Employee's Bonus ";
            else
                msg = "Please fill Employee's Rate ";
            txtBonusRate.findFocus();
            isValid = false;
        }

        if(!isValid) {
            alertMessage(MainActivity.this,"Alert", msg);
        }
        return isValid;
    }

    public void clearField(boolean shouldClearName){
        if(!shouldClearName) {
            txtName.setText("");
            txtAge.setText("");
            txtDOB.setText("");
            txtCountry.setText("");
            txtSalaryHoursSchool.setText("");
            txtBonusRate.setText("");
            txtPlate.setText("");
            txtMake.setText("");
        }
    }

    private void display (Employee e) {
        clearField(false);

        if (e instanceof FullTime) {
            radioEmployeeGroup.check(R.id.radioFullTime);
            txtSalaryHoursSchool.setText(String.valueOf(((FullTime) e).salary));
            txtBonusRate.setText(String.valueOf(((FullTime) e).bonus));
        } else if (e instanceof PartTime) {
            radioEmployeeGroup.check(R.id.radioPartTime);
            txtSalaryHoursSchool.setText(String.valueOf(((PartTime) e).hoursWorked));
            txtBonusRate.setText(String.valueOf(((PartTime) e).rate));
        } else if (e instanceof Intern) {
            radioEmployeeGroup.check(R.id.radioIntern);
            txtSalaryHoursSchool.setText(((Intern) e).collegeName);
        }

        // I put here because of bug ???
        txtName.setText(e.getName());
        txtAge.setText(String.valueOf(e.getAge()));
        txtDOB.setText(e.getDOB());
        txtCountry.setText(e.getCountry());

        if (e.vehicle_owned != null) {
            txtPlate.setText(e.vehicle_owned.plateNumber);
            txtMake.setText(e.vehicle_owned.make);
        }
    }

    private void changeEditState(Boolean on) {
        if (on) {
            isEdit = true;
            btnAddChange.setText("Change");
        } else {
            isEdit = false;
            currentEmployee = null;
            btnAddChange.setText("Add");
        }
    }

    public static void alertMessage(Context context, String title, String message){
        /*
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("OK", null);
        dialog.show();
        */
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void saveSharedPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.remove("employeelist").commit();
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(MainActivity.arrEmployee.getClass(), new EmployeeListSerializer());
        Gson gson = gb.create();
        String json = gson.toJson(MainActivity.arrEmployee);
        Log.d(TAG,"saveSharedPreferences: "+json);
        prefsEditor.putString("employeelist", json).commit();
    }

    public static void loadSharedPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        //SharedPreferences.Editor prefsEditor = prefs.edit();
        //prefsEditor.remove("employeelist").commit();
        String json = prefs.getString("employeelist", "");
        Log.d(TAG,"loadSharedPreferences: "+json);
        if (!json.isEmpty()) {
            GsonBuilder gb = new GsonBuilder();
            gb.registerTypeAdapter(MainActivity.arrEmployee.getClass(), new EmployeeListDeserializer());
            Gson gson = gb.create();
            MainActivity.arrEmployee = gson.fromJson(json, MainActivity.arrEmployee.getClass());
        }
        MainActivity.isLoad = true;
    }

    public static class EmployeeListSerializer implements JsonSerializer<ArrayList<Employee>> {
        @Override
        public JsonElement serialize(ArrayList<Employee> src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return null;
            else {
                JsonArray ja = new JsonArray();
                for (Employee emp : src) {
                    Class classType = Employee.class;
                    if (emp instanceof FullTime) classType = FullTime.class;
                    else if (emp instanceof PartTime) classType = PartTime.class;
                    else if (emp instanceof Intern) classType = Intern.class;

                    if (classType != null) ja.add(context.serialize(emp, classType));
                }
                return ja;
            }
        }
    }

    public static class EmployeeListDeserializer implements JsonDeserializer<List<Employee>> {

        @Override
        public List<Employee> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List list = new ArrayList<Employee>();
            JsonArray ja = json.getAsJsonArray();
            for (JsonElement je : ja) {
                Class classType = Employee.class;
                String value = null;
                try {
                    value =  je.getAsJsonObject().get("salary").getAsString();
                    classType = FullTime.class;
                } catch (Exception e) {
                    e.getStackTrace();
                }
                if(value == null) {
                    try {
                        value =  je.getAsJsonObject().get("hoursWorked").getAsString();
                        classType = PartTime.class;
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                    if(value == null) {
                        try {
                            value = je.getAsJsonObject().get("collegeName").getAsString();
                            classType = Intern.class;
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                }

                if (classType != null) list.add(context.deserialize(je, classType));
            }
            return list;
        }
    }


}
