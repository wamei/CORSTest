package jp.wamei.corstest

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebView
import android.content.Context
import android.util.Log
import android.webkit.WebSettings
import java.io.IOException
import java.io.InputStream
import fi.iki.elonen.NanoHTTPD

class MainActivity : AppCompatActivity() {
    private var context: Context? = null
    private var webServer: WebServer? = null
    private val PORT = 8080

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.context = applicationContext
        this.webServer = WebServer(applicationContext)
        try {
            this.webServer?.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        val webView = findViewById(R.id.webView) as WebView
        webView.settings.javaScriptEnabled = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.loadUrl("http://127.0.0.1:8080/")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this.webServer != null) {
            this.webServer?.stop()
        }
    }

    private inner class WebServer(private val context: Context) : NanoHTTPD(PORT) {

        override fun serve(session: IHTTPSession): NanoHTTPD.Response {
            try {
                var uri = session.uri
                if ("/".equals(uri)) {
                    uri = "index.html"
                }

                var filename = uri
                if (uri.substring(0, 1).equals("/")) {
                    filename = filename.substring(1)
                }
                Log.d("AppInfo", filename)

                val asssets = context.getResources().getAssets()
                var fis: InputStream? = null
                try {
                    fis = asssets.open(filename)
                } catch (e: Exception) {
                    Log.d("AppErr", "File openning failed")
                }

                if (uri.endsWith(".ico")) {
                    return NanoHTTPD.newChunkedResponse(Response.Status.OK, "image/x-icon", fis)
                } else if (uri.endsWith(".png") || uri.endsWith(".PNG")) {
                    return NanoHTTPD.newChunkedResponse(Response.Status.OK, "image/png", fis)
                } else if (uri.endsWith(".jpg") || uri.endsWith(".JPG") || uri.endsWith(".jpeg") || uri.endsWith(".JPEG")) {
                    return NanoHTTPD.newChunkedResponse(Response.Status.OK, "image/jpeg", fis)
                } else if (uri.endsWith(".js")) {
                    return NanoHTTPD.newChunkedResponse(Response.Status.OK, "application/javascript", fis)
                } else if (uri.endsWith(".css")) {
                    return NanoHTTPD.newChunkedResponse(Response.Status.OK, "text/css", fis)
                } else if (uri.endsWith(".html") || uri.endsWith(".htm")) {
                    return NanoHTTPD.newChunkedResponse(Response.Status.OK, "text/html", fis)
                } else if (uri.endsWith(".map")) {
                    return NanoHTTPD.newChunkedResponse(Response.Status.OK, "application/json", fis)
                } else {
                    return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", uri)
                }
            } catch (ioe: IOException) {
                ioe.printStackTrace()
                return NanoHTTPD.newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", ioe.localizedMessage)
            }
        }
    }
}
