import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.token.verifysdk.VerifyCoder;

/**
 * @author Julian Chu
 * @since 2017/12/21
 */

public class TencentVerifyCoderDialogFragment extends DialogFragment {

    private OnTencentVerifyCoderListener mTencentVerifyCoderListener;

    private VerifyCoder.VerifyListener mVerifyListener = new VerifyCoder.VerifyListener() {
        public void onVerifySucc(String ticket, String randstr) {
            if (mTencentVerifyCoderListener != null) {
                mTencentVerifyCoderListener.onVerifySucc(ticket, randstr);
            }
            dismiss();
        }

        public void onVerifyFail() {
            if (mTencentVerifyCoderListener != null) {
                mTencentVerifyCoderListener.onVerifyFail();
            }
        }
    };

    public static TencentVerifyCoderDialogFragment newInstance(String jsurl) {

        Bundle args = new Bundle();
        args.putString("jsurl", jsurl);

        TencentVerifyCoderDialogFragment fragment = new TencentVerifyCoderDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTencentVerifyCoderListener) {
            mTencentVerifyCoderListener = (OnTencentVerifyCoderListener) context;
        } else {
            throw new ClassCastException("context must implement OnTencentVerifyCoderListener")
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTencentVerifyCoderListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String jsurl = getArguments().getString("jsurl");
        if (jsurl == null) {
            dismiss();
        }
        WebView webView = VerifyCoder.getVerifyCoder().getWebView(getContext(), jsurl, mVerifyListener);
        webView.requestFocus();
        webView.forceLayout();
        return webView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        VerifyCoder.getVerifyCoder().release();
    }

    public interface OnTencentVerifyCoderListener {
        void onVerifySucc(String ticket, String randstr);

        void onVerifyFail();
    }
}
