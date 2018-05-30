package com.vedant.hereami.chatfolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vedant.hereami.Fragment.CallsFragment;
import com.vedant.hereami.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MessageChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String seedValue = "youcantseeme@9900";
    private final String todaycheck;
    private final String encrytionprivatekey1;
    private List<MessageChatModel> mListOfFireChat;
    private static final int SENDER = 0;
    private static final int RECIPIENT = 1;
    TimeZone pdt = TimeZone.getTimeZone("UTC");


    public static final String mypreference1 = "privatekey";
    public static final String mypreference123 = "mypref123";
    Context mcontext;


    public MessageChatAdapter(List<MessageChatModel> listOfFireChats, String encrytionprivatekey) {

        encrytionprivatekey1 = encrytionprivatekey;
        mListOfFireChat = listOfFireChats;
        Calendar calendar1 = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar1.setTime(trialTime);
        Date now = new Date();
        int date1 = calendar1.get(Calendar.DATE);
        int month1 = calendar1.get(Calendar.MONTH);
        int year1 = calendar1.get(Calendar.YEAR);
        todaycheck = date1 + "/" + month1 + "/" + year1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListOfFireChat.get(position).getRecipientOrSenderStatus() == SENDER) {
            Log.e("Adapter", " sender");
            return SENDER;
        } else {
            return RECIPIENT;
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case SENDER:
                View viewSender = inflater.inflate(R.layout.sender_message, viewGroup, false);
                viewHolder = new ViewHolderSender(viewSender);
                break;
            case RECIPIENT:
                View viewRecipient = inflater.inflate(R.layout.recipient_message, viewGroup, false);
                viewHolder = new ViewHolderRecipient(viewRecipient);
                break;
            default:
                View viewSenderDefault = inflater.inflate(R.layout.sender_message, viewGroup, false);
                viewHolder = new ViewHolderSender(viewSenderDefault);
                break;
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (viewHolder.getItemViewType()) {
            case SENDER:
                ViewHolderSender viewHolderSender = (ViewHolderSender) viewHolder;
                configureSenderView(viewHolderSender, position);
                break;
            case RECIPIENT:
                ViewHolderRecipient viewHolderRecipient = (ViewHolderRecipient) viewHolder;
                configureRecipientView(viewHolderRecipient, position);
                break;
        }


    }

    private void configureSenderView(ViewHolderSender viewHolderSender, int position) {
        MessageChatModel senderFireMessage = mListOfFireChat.get(position);
        if (senderFireMessage.getImageurl1() == null) {
            viewHolderSender.mSenderimageview.setVisibility(View.INVISIBLE);

            String encrypted = senderFireMessage.getMessage1();
            String decrypted = CallsFragment.decryptRSAToString(encrypted, encrytionprivatekey1);
            viewHolderSender.getSenderMessageTextView().setText(decrypted);
            String s;
            String[] parts1 = senderFireMessage.getTimestamp().split("%");
            String part4 = parts1[0];
            String part5 = parts1[1];
            String timecheck = null;
            String dateset = null;

            SimpleDateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.ENGLISH);
            SimpleDateFormat df1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            //    SimpleDateFormat df1 = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);

            try {
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = df.parse(senderFireMessage.getTimestamp().replace("%", " "));
                df1.setTimeZone(TimeZone.getDefault());
                timecheck = df1.format(date);
                df2.setTimeZone(TimeZone.getDefault());
                dateset = df2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!part5.equals(todaycheck)) {
                //  s = part5;
                s = dateset;
            } else {
                //  s = part4;
                s = timecheck;
            }
            viewHolderSender.getSenderMessagetimeTextView().setText(s);
        } else {
            viewHolderSender.mSenderMessageTextView.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolderSender.mSenderimageview.getContext()).load(senderFireMessage.getImageurl1()).placeholder(R.drawable.headshot_7).into(viewHolderSender.mSenderimageview);

        }

    }

    private void configureRecipientView(ViewHolderRecipient viewHolderRecipient, int position) {

        MessageChatModel recipientFireMessage = mListOfFireChat.get(position);
        if (recipientFireMessage.getImageurl1() == null) {
            viewHolderRecipient.mReecepientimageview.setVisibility(View.INVISIBLE);
            String encrypted = recipientFireMessage.getMessage();
            String decrypted;
            if (encrypted != null) {
                decrypted = CallsFragment.decryptRSAToString(encrypted, encrytionprivatekey1);


                //   String decrypted = KeyStoreHelper.decrypt(KEYSTORE_KEY_ALIAS, encrypted);
                viewHolderRecipient.getRecipientMessageTextView().setText(decrypted);
                String s;
                String[] parts1 = recipientFireMessage.getTimestamp().split("%");
                String part4 = parts1[0];
                String part5 = parts1[1];

                if (!part5.equals(todaycheck)) {
                    s = part5;
                } else {
                    s = part4;
                }
                viewHolderRecipient.getRecipientMessagetimeTextView().setText(s);
            } else {
                viewHolderRecipient.getRecipientMessageTextView().setText(encrypted);
                String s;
                String[] parts1 = recipientFireMessage.getTimestamp().split("%");
                String part4 = parts1[0];
                String part5 = parts1[1];

                if (!part5.equals(todaycheck)) {
                    s = part5;
                } else {
                    s = part4;
                }
                viewHolderRecipient.getRecipientMessagetimeTextView().setText(s);
            }
        } else {

            viewHolderRecipient.mRecipientMessageTextView.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolderRecipient.mReecepientimageview.getContext()).load(recipientFireMessage.getImageurl1()).placeholder(R.drawable.headshot_7).into(viewHolderRecipient.mReecepientimageview);

        }
    }


    @Override
    public int getItemCount() {
        return mListOfFireChat.size();
    }


    public void refillAdapter(MessageChatModel newFireChatMessage) {

        /*add new message1 chat to list*/

        mListOfFireChat.add(newFireChatMessage);

        /*refresh view*/
        //   notifyItemInserted(getItemCount() - 1);
    }

    public void refillFirsTimeAdapter(List<MessageChatModel> newFireChatMessage) {

        /*add new message1 chat to list*/
        mListOfFireChat.clear();

        mListOfFireChat.addAll(newFireChatMessage);
        /*refresh view*/
        notifyItemInserted(getItemCount() - 1);
    }

    public void cleanUp() {
        mListOfFireChat.clear();
    }


    /*==============ViewHolder===========*/

    /*ViewHolder for Sender*/

    public class ViewHolderSender extends RecyclerView.ViewHolder {

        private TextView mSenderMessageTextView;
        private TextView mSenderMessagetimeTextView;
        private ImageView mSenderimageview;

        public ViewHolderSender(View itemView) {
            super(itemView);
            mSenderMessageTextView = itemView.findViewById(R.id.senderMessage);
            mSenderMessagetimeTextView = itemView.findViewById(R.id.senderMessagetime);
            mSenderimageview = itemView.findViewById(R.id.imageView_sender);
        }

        public ImageView getmSenderimageview() {
            return mSenderimageview;
        }

        public TextView getSenderMessageTextView() {
            return mSenderMessageTextView;
        }

        public TextView getSenderMessagetimeTextView() {
            return mSenderMessagetimeTextView;
        }

        public void setSenderMessageTextView(TextView senderMessage) {
            mSenderMessageTextView = senderMessage;
        }

        public void setSenderMessagetimeTextView(TextView senderMessagetime) {
            mSenderMessagetimeTextView = senderMessagetime;
        }
    }


    /*ViewHolder for Recipient*/
    public class ViewHolderRecipient extends RecyclerView.ViewHolder {

        private TextView mRecipientMessageTextView;
        private TextView mRecipientMessagetimeTextView;
        private ImageView mReecepientimageview;

        public ViewHolderRecipient(View itemView) {
            super(itemView);
            mRecipientMessageTextView = itemView.findViewById(R.id.recipientMessage);
            mRecipientMessagetimeTextView = itemView.findViewById(R.id.recipientMessagetime);
            mReecepientimageview = itemView.findViewById(R.id.imageView_recepient);
        }

        public ImageView getmReecepientimageview() {
            return mReecepientimageview;
        }

        public TextView getRecipientMessageTextView() {
            return mRecipientMessageTextView;
        }

        public TextView getRecipientMessagetimeTextView() {
            return mRecipientMessagetimeTextView;
        }

        public void setRecipientMessageTextView(TextView recipientMessage) {
            mRecipientMessageTextView = recipientMessage;
        }

        public void setRecipientMessagetimeTextView(TextView recipientMessagetime) {
            mRecipientMessagetimeTextView = recipientMessagetime;
        }
    }



}