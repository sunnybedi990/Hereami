package com.vedant.hereami.chatfolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedant.hereami.Fragment.CallsFragment;
import com.vedant.hereami.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


public class MessageChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String seedValue = "youcantseeme@9900";
    private final String todaycheck;
    private final String encrytionprivatekey1;
    private List<MessageChatModel> mListOfFireChat;
    private static final int SENDER = 0;
    private static final int RECIPIENT = 1;
    TimeZone pdt = TimeZone.getDefault();

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
        String encrypted = senderFireMessage.getMessage1();
        String decrypted = CallsFragment.decryptRSAToString(encrypted, encrytionprivatekey1);
        viewHolderSender.getSenderMessageTextView().setText(decrypted);
        String s;
        String[] parts1 = senderFireMessage.getTimestamp().split("%");
        String part4 = parts1[0];
        String part5 = parts1[1];

        if (!part5.equals(todaycheck)) {
            s = part5;
        } else {
            s = part4;
        }
        viewHolderSender.getSenderMessagetimeTextView().setText(s);

    }

    private void configureRecipientView(ViewHolderRecipient viewHolderRecipient, int position) {

        MessageChatModel recipientFireMessage = mListOfFireChat.get(position);
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

        public ViewHolderSender(View itemView) {
            super(itemView);
            mSenderMessageTextView = (TextView) itemView.findViewById(R.id.senderMessage);
            mSenderMessagetimeTextView = (TextView) itemView.findViewById(R.id.senderMessagetime);
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

        public ViewHolderRecipient(View itemView) {
            super(itemView);
            mRecipientMessageTextView = (TextView) itemView.findViewById(R.id.recipientMessage);
            mRecipientMessagetimeTextView = (TextView) itemView.findViewById(R.id.recipientMessagetime);
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


    public String decryption(String strEncryptedText) {

        String strDecryptedText = "";
        try {
            strDecryptedText = AESHelper.decrypt(seedValue, strEncryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDecryptedText;
    }
}