package com.adi.coldcall.activities;

import static com.adi.coldcall.Utils.checkCallLogPermission;
import static com.adi.coldcall.Utils.checkCallPermission;
import static com.adi.coldcall.Utils.getLastCallMade;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.coldcall.CallAdapter;
import com.adi.coldcall.MainViewModel;
import com.adi.coldcall.R;
import com.adi.coldcall.Utils;
import com.adi.coldcall.databinding.ActivityMainBinding;
import com.adi.coldcall.models.Call;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MainViewModel viewModel;

    CallAdapter callAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkCallLogPermission(this)) return;
        if (viewModel == null) return;

        insertCallEntry();
    }

    private void insertCallEntry() {
        if (viewModel.getLastCallNumber() != null) {
            /*
                receives an object of @Call if out of last 10 outgoing call entries
                a entry with the @lastCallNumber is found.
                else null is returned
             */
            Call call = getLastCallMade(this, viewModel.getLastCallNumber());

            if (call != null) {
                /*
                    if the call duration is greater than zero
                    set @status of call to STATUS_CALL_ANSWERED
                    or else to STATUS_CALL_NOT_ANSWERED
                 */
                if (call.getDuration() > 0) call.setStatus(Call.STATUS_CALL_ANSWERED);
                else call.setStatus(Call.STATUS_CALL_NOT_ANSWERED);

                /*
                    inserting call log determined to the offline database
                 */
                viewModel.insetCallEntry(call);

                /*
                    setting @lastCallNumber to null
                    to ensure that next time when onStart() is invoked no same entry is added again
                    to the database
                 */
                viewModel.setLastCallNumber(null);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.callBtn.setOnClickListener(this::validatePhoneNumberAndCall);

        /*
            adapter for the recycler view
         */
        callAdapter = new CallAdapter(this);
        binding.callsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.callsRecyclerView.setAdapter(callAdapter);

        /*
            setting a click listener to phone icon on CallViewHolder
         */
        callAdapter.setCallClickListener(this::makeACall);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        /*
            Observing live data from the database
            on data changed result (List of Calls) is submitted to adapter
         */
        viewModel.getCalls().observe(this, results -> {
            /*
                submitting result retrieved from database
             */
            callAdapter.submitList(results);

            if (results.size() > 0) {
                /*
                    making recycler view visible only if result is available
                    and making no data msg disappear
                 */
                binding.callsRecyclerView.setVisibility(View.VISIBLE);
                binding.notDataMsg.setVisibility(View.GONE);

                /*
                    scrolling it to first position i.e. the new entry
                 */
                binding.callsRecyclerView.smoothScrollToPosition(0);
            } else {
                /*
                    making recyclerview disappear as no data available
                    and making visible @noDataMsg
                 */
                binding.callsRecyclerView.setVisibility(View.GONE);
                binding.notDataMsg.setVisibility(View.VISIBLE);
            }

            binding.phoneEt.setText(null);
        });

        /*
            attaching swipe listener to recycler view
            on swipe deleting the element from the database
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.deleteCallEntry(callAdapter.getCallAtPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(binding.callsRecyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
            setting viewMode to null on activity finish
         */
        viewModel = null;
    }

    /*
        Validating phone number entered in EditText and launching the call intent
    */
    private void validatePhoneNumberAndCall(View v) {
        String phoneNumber = binding.phoneEt.getText().toString();
        if (phoneNumber.trim().isEmpty() || phoneNumber.length() != 10) {
            binding.phoneEt.setError("Invalid phone number");
            return;
        }

        makeACall(phoneNumber);
    }

    private void makeACall(String phoneNumber) {
        if (checkCallPermission(this)) {
            /*
                setting @lastCallNumber to the number user is now going to call
                with the phone number entered in editText.

                @lastCallNumber would then be used to search in call logs
             */
            viewModel.setLastCallNumber("+91" + phoneNumber);

            Utils.makeACall(phoneNumber, this);
        }
    }
}