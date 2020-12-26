package com.omdhanwant.permissionsbackgroundtasksmedia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

    WebView mWebView;
    EditText etUrl;
    String Url;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUrl = (EditText) view.findViewById(R.id.etUrl);
        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebClient());
        mWebView.loadUrl("https://www.udemy.com");


        view.findViewById(R.id.buGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
                Url = etUrl.getText().toString();
//                if(!Url.equals("")) {
                    mWebView.loadUrl(Url);
//                } else{
//                    Toast.makeText(getContext(),"Please enter the url",Toast.LENGTH_LONG).show();
//                }

            }
        });

        view.findViewById(R.id.buBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.goBack();
            }
        });

        view.findViewById(R.id.buForward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mWebView.goForward();
            }
        });
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(Url);
            return true;
        }
    }
}