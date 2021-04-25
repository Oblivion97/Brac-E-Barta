package com.brac.bracebatra.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.brac.bracebatra.R;
import com.brac.bracebatra.model.Institute;
import com.brac.bracebatra.ui.adapter.SchoolAdapter;
import com.brac.bracebatra.util.util;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

public class ProgramOrganizerActivity extends AppCompatActivity {
    /**
     * school list to input attendance
     */
    List<Institute> instituteList = new ArrayList<Institute>();
    RecyclerView recyclerView;
    SchoolAdapter adapter;

    TextView tvPoName;
    TextView tvPoId;
    private ImageButton ibHb;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_organizer);

        tvPoName = (TextView) findViewById(R.id.tv_po_name);
        ibHb = (ImageButton) findViewById(R.id.ib_hb_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(util.CURRENT_PO);
        tvPoName.setText("Po Name :" + util.CURRENT_PO);
        adapter = new SchoolAdapter(instituteList, getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.rv_po_main);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setAdapter(adapter);

        try {
            loadDate();
        } catch (NullPointerException e) {
            Log.d("Error", "exception");
        }

        // navigation drawer
        Drawer();

    }

    private void loadDate() {
        List<Institute> institutes = new Select().from(Institute.class).where("poId = ?", util.CURRENT_PO).execute();
        if (institutes.size() > 0) {
            instituteList.addAll(institutes);
        }
        Toast.makeText(ProgramOrganizerActivity.this, "Total School =" + institutes.size() + " " + util.CURRENT_PO, Toast.LENGTH_SHORT).show();
    }

    public void Drawer() {

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthDp(250)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withActivity(ProgramOrganizerActivity.this)
                .withFullscreen(false)
                .withCloseOnClick(true)

                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Sync Data").withSetSelected(true).withIdentifier(1),
                        new PrimaryDrawerItem().withName("School List").withSetSelected(true).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Student List").withSetSelected(true).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Logout").withSetSelected(true).withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {


                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.equals(1)) {

                            startActivity(new Intent(ProgramOrganizerActivity.this, SyncActivity.class));
                        }
                        if (drawerItem.equals(0)) {


                        }
                        if (drawerItem.equals(2)) {

                            startActivity(new Intent(ProgramOrganizerActivity.this, ProgramOrganizerActivity.class));

                        }
                        if (drawerItem.equals(3)) {

                            startActivity(new Intent(ProgramOrganizerActivity.this, StudentListActivity.class));

                        }
                        if (drawerItem.equals(4)) {
                            startActivity(new Intent(ProgramOrganizerActivity.this, MainActivity.class));
                            finish();

                        }
                        return true;
                    }
                }).withDrawerGravity(Gravity.LEFT)
                .build();
        result.openDrawer();
        result.closeDrawer();
        result.isDrawerOpen();

        // hamburger icon click
        ibHb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.openDrawer();
            }
        });
    }
}
