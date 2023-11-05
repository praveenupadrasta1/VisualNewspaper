package Miscellaneous;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.praveenupadrasta.news.R;

/**
 * Created by praveenupadrasta on 22-05-2017.
 */

public class DialogBox {

    public Dialog createDialogBox(String msg, Context context)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.info_dialog);
        TextView txtInfo = (TextView) dialog.findViewById(R.id.txt_info);
        txtInfo.setText(msg);
        dialog.setTitle("Info...");
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
