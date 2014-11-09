package com.boha.dialer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.boha.dialer.R;
import com.boha.dialer.util.Util;
import com.boha.ericssen.library.util.CallIntentService;
import com.boha.ericssen.library.util.CustomToast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class DialerFragment extends Fragment implements PageFragment {

    public DialerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    TextView txtCount, txtName;
    Button btnNormal, btnPay4Me;
    boolean isPay4MeCall;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dialer, container, false);
        ctx = getActivity();
        setFields();
        Bundle b = getArguments();
        if (b != null) {

        }


        return view;
    }

    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_DTMF, 50);

    private void setFields() {
        btnNormal = (Button) view.findViewById(R.id.TOG_btnNormal);
        btnPay4Me = (Button) view.findViewById(R.id.TOG_btnPay4Me);
        circle = (TextView) view.findViewById(R.id.TOG_circle);
        circle.setVisibility(View.GONE);
        txtPhoneNumber  = (TextView) view.findViewById(R.id.DIAL_phoneNumber);
        txtPhoneNumber.setText("");

        number0 = view.findViewById(R.id.DIAL_numLayout0);
        number1 = view.findViewById(R.id.DIAL_numLayout1);
        number2 = view.findViewById(R.id.DIAL_numLayout2);
        number3 = view.findViewById(R.id.DIAL_numLayout3);
        number4 = view.findViewById(R.id.DIAL_numLayout4);
        number5 = view.findViewById(R.id.DIAL_numLayout5);
        number6 = view.findViewById(R.id.DIAL_numLayout6);
        number7 = view.findViewById(R.id.DIAL_numLayout7);
        number8 = view.findViewById(R.id.DIAL_numLayout8);
        number9 = view.findViewById(R.id.DIAL_numLayout9);

        hash = view.findViewById(R.id.DIAL_numLayoutHash);
        star = view.findViewById(R.id.DIAL_numLayoutStar);


        imgErase = (ImageView) view.findViewById(R.id.DIAL_imgBackspace);
        btnCallNormal = (Button) view.findViewById(R.id.DIAL_btnCallNormal);
        btnCallPay4Me = (Button) view.findViewById(R.id.DIAL_btnCallPay4Me);

        number0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("0");
                tg.startTone(ToneGenerator.TONE_DTMF_0, 100);

            }
        });
        number1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("1");
                tg.startTone(ToneGenerator.TONE_DTMF_1, 100);
            }
        });
        number2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("2");
                tg.startTone(ToneGenerator.TONE_DTMF_2, 100);
            }
        });
        number3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("3");
                tg.startTone(ToneGenerator.TONE_DTMF_3, 100);
            }
        });
        number4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("4");
                tg.startTone(ToneGenerator.TONE_DTMF_4, 100);
            }
        });
        number5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("5");
                tg.startTone(ToneGenerator.TONE_DTMF_5, 100);
            }
        });
        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("6");
                tg.startTone(ToneGenerator.TONE_DTMF_6, 100);
            }
        });
        number7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("7");
                tg.startTone(ToneGenerator.TONE_DTMF_7, 100);
            }
        });
        number8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("8");
                tg.startTone(ToneGenerator.TONE_DTMF_8, 100);
            }
        });
        number9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("9");
                tg.startTone(ToneGenerator.TONE_DTMF_9, 100);
            }
        });
        //


        //
        hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("#");
                tg.startTone(ToneGenerator.TONE_DTMF_A, 100);
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEntry("*");
                tg.startTone(ToneGenerator.TONE_DTMF_B, 100);
            }
        });
        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNormal.setTextColor(ctx.getResources().getColor(R.color.white));
                isPay4MeCall = false;
                btnPay4Me.setTextColor(ctx.getResources().getColor(R.color.translucent_red));
                circle.setVisibility(View.GONE);
                changeColors(ctx.getResources().getColor(R.color.black));
                btnCallPay4Me.setVisibility(View.GONE);
                btnCallNormal.setVisibility(View.VISIBLE);
                Util.animateScaleX(btnCallNormal,300);
                txtPhoneNumber.setTextColor(ctx.getResources().getColor(R.color.white));

            }
        });
        btnPay4Me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNormal.setTextColor(ctx.getResources().getColor(R.color.green));
                isPay4MeCall = true;
                btnPay4Me.setTextColor(ctx.getResources().getColor(R.color.yellow));
                circle.setVisibility(View.VISIBLE);
                Util.animateRotationY(circle,1000);
                changeColors(ctx.getResources().getColor(R.color.absa_red));
                btnCallPay4Me.setVisibility(View.VISIBLE);
                btnCallNormal.setVisibility(View.GONE);
                Util.animateScaleX(btnCallPay4Me,300);
                txtPhoneNumber.setTextColor(ctx.getResources().getColor(R.color.yellow));
            }
        });
        txtPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLastEntry();
            }
        });
        txtPhoneNumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txtPhoneNumber.setText("");
                numList = new ArrayList<String>();
                refreshEntries();
                return true;
            }
        });
        txtPhoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    txtPhoneNumber.setText("");
                    numList = new ArrayList<String>();
                    refreshEntries();
                    return true;
                }
                return false;
            }
        });
        btnCallNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCall(NORMAL_CALL, txtPhoneNumber.getText().toString());
            }
        });
        btnCallPay4Me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCall(PAY4ME_CALL, txtPhoneNumber.getText().toString());
            }
        });
    }

    static final int NORMAL_CALL = 1, PAY4ME_CALL = 2;
    static final String MTN_PREFIX = "127";
    private void startCall(int type, String number) {
        endCallListener = new EndCallListener();
        if (telephonyManager == null) {
            telephonyManager =
                    (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(endCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        switch (type) {
            case NORMAL_CALL:
                break;
            case PAY4ME_CALL:
                number = MTN_PREFIX + number;
                break;
        }

        Intent i = new Intent(ctx, CallIntentService.class);
        i.putExtra(CallIntentService.EXTRA_NUMBER, number);
        ctx.startService(i);
//
//        String uri = "tel:" + number;
//        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
//        Log.w("DialerFragment","###### calling: " + uri);
//        startActivity(callIntent);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialerFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement DialerFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {


    }

    DialerFragmentListener mListener;

    public interface DialerFragmentListener {

    }

    View number0, number1, number2, number3, number4,
            number5, number6, number7, number8, number9, hash, star;

    ImageView  imgErase;
    TextView circle, txtPhoneNumber;
    Button btnCallNormal, btnCallPay4Me;
    List<String> numList = new ArrayList<String>();
    private void addEntry(String entry) {
        numList.add(entry);
        refreshEntries();
    }
    private void removeLastEntry() {
        if (numList.isEmpty()) {
            return;
        }
        try {
            numList.remove(numList.size() - 1);
            refreshEntries();
        }catch (Exception e) {}
    }

    private void refreshEntries() {
        StringBuilder sb = new StringBuilder();
        for (String digit: numList) {
            sb.append(digit);
        }

        txtPhoneNumber.setText(Util.formatCellphone(sb.toString()));
    }
    private void changeColors(int color) {
        TextView txt0 = (TextView) number0.findViewById(R.id.DIAL_0);
        txt0.setTextColor(color);
        waitABit(txt0);

        TextView txt1 = (TextView) number1.findViewById(R.id.DIAL_1);
        txt1.setTextColor(color);
        waitABit(txt1);

        TextView txt2 = (TextView) number2.findViewById(R.id.DIAL_2);
        txt2.setTextColor(color);
        waitABit(txt2);

        TextView txt3 = (TextView) number3.findViewById(R.id.DIAL_3);
        txt3.setTextColor(color);
        waitABit(txt3);

        TextView txt4 = (TextView) number4.findViewById(R.id.DIAL_4);
        txt4.setTextColor(color);
        waitABit(txt4);

        TextView txt5 = (TextView) number5.findViewById(R.id.DIAL_5);
        txt5.setTextColor(color);
        waitABit(txt5);

        TextView txt6 = (TextView) number6.findViewById(R.id.DIAL_6);
        txt6.setTextColor(color);
        waitABit(txt6);

        TextView txt7 = (TextView) number7.findViewById(R.id.DIAL_7);
        txt7.setTextColor(color);
        waitABit(txt7);

        TextView txt8 = (TextView) number8.findViewById(R.id.DIAL_8);
        txt8.setTextColor(color);
        waitABit(txt8);

        TextView txt9 = (TextView) number9.findViewById(R.id.DIAL_9);
        txt9.setTextColor(color);
        waitABit(txt9);
    }
    private void waitABit(TextView v) {
        Util.animateRotationY(v, 300);
    }
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###,###,###,###");
    private class EndCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(TelephonyManager.CALL_STATE_RINGING == state) {
                Log.i(LOG, "RINGING, number: " + incomingNumber);
            }
            if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
                //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
                CustomToast t = new CustomToast(getActivity());
                t.setGravity(Gravity.TOP, 0,0);
                t.setDuration(8);
                LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inf.inflate(com.boha.ericssen.library.R.layout.toast_calling,null);
                TextView tv = (TextView) v.findViewById(com.boha.ericssen.library.R.id.TC_txtNumber);
                if (isPay4MeCall) {
                    tv.setTextColor(ctx.getResources().getColor(R.color.absa_red));
                } else {
                    tv.setTextColor(ctx.getResources().getColor(R.color.green));
                }
                tv.setText(txtPhoneNumber.getText().toString());
                t.setView(v);
                t.show();
                Log.i(LOG, "OFFHOOK " + incomingNumber);
            }
            if(TelephonyManager.CALL_STATE_IDLE == state) {
                //when this state occurs, and your flag is set, restart your app
//                Toast t = Toast.makeText(ctx, "", Toast.LENGTH_LONG);
//                t.setGravity(Gravity.TOP, 0,0);
//                LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View v = inf.inflate(com.boha.ericssen.library.R.layout.toast_calling,null);
//                TextView tv = (TextView) v.findViewById(com.boha.ericssen.library.R.id.TC_txtNumber);
//                tv.setText(txtPhoneNumber.getText().toString());
//                t.setView(inf.inflate(com.boha.ericssen.library.R.layout.toast_calling, null));
//                t.show();
                Log.i(LOG, "IDLE");
                //Toast.makeText(ctx, "Detected call hangup event, idle: " + incomingNumber, Toast.LENGTH_LONG).show();
            }
            if (TelephonyManager.CALL_STATE_RINGING == state) {

            }
        }
    }
    TelephonyManager telephonyManager;
    EndCallListener endCallListener;
    static final String LOG = DialerFragment.class.getSimpleName();
}
