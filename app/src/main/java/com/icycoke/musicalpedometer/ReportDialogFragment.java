package com.icycoke.musicalpedometer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ReportDialogFragment extends DialogFragment {

    private int walkingTime;
    private int runningTime;
    private int stepCount;

    public ReportDialogFragment(int walkingTime, int runningTime, int stepCount) {
        this.walkingTime = walkingTime;
        this.runningTime = runningTime;
        this.stepCount = stepCount;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        StringBuilder report = new StringBuilder();
        report.append("Total time: ").append(walkingTime + runningTime).append("s").append('\n')
                .append("Walking time: ").append(walkingTime).append("s").append('\n')
                .append("Running time: ").append(runningTime).append("s").append('\n')
                .append("Step count: ").append(stepCount);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(report)
                .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReportDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
