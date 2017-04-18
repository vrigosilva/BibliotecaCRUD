package br.com.fiap.biblioteca;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.fiap.biblioteca.Modelo.Usuario;
import br.com.fiap.biblioteca.dao.UsuarioDAO;

public class SplashScreen extends AppCompatActivity {

    //Tempo que splashscreen ficará visivel
    private final int SPLASH_DISPLAY_LENGTH = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        carregar();
        new BuscaDados().execute("http://www.mocky.io/v2/58b9b1740f0000b614f09d2f");
    }


    private void carregar() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.animacao_splash);
        anim.reset();

        //Pegando o objeto criado no layout
        ImageView iv = (ImageView) findViewById(R.id.splash);
        if (iv != null) {
            iv.clearAnimation();
            iv.startAnimation(anim);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Após o tempo definido irá executar a próxima  tela

                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }





    // url para fazer o download, progresso (loading)
    private class BuscaDados extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);

                conn.setRequestMethod("GET");
                conn.setDoOutput(true);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                    InputStream is = conn.getInputStream();
                    BufferedReader buffer =  new BufferedReader((new InputStreamReader(is)));

                    StringBuilder result = new StringBuilder();
                    String linha;

                    // enquanto receber a leitura de linha
                    while ((linha = buffer.readLine()) != null) {
                        result.append(linha);
                    }

                    // fechar a conexão
                    conn.disconnect();
                    return result.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
             super.onPostExecute(s);
            if (s == null) {
                Toast.makeText(SplashScreen.this, "Falha na sincronização", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject data = new JSONObject(s);
                    Usuario usuario = new Usuario();
                    usuario.setUsuario(data.getString("usuario"));
                    usuario.setSenha(data.getString("senha"));
                    UsuarioDAO dao = new UsuarioDAO(SplashScreen.this);
                    if( !dao.remove()) {
                        throw new Exception("Falha no acesso a dados");
                    }

                    if( !dao.persist(usuario)) {
                        throw new Exception("Falha no acesso a dados");
                    }

                    Toast.makeText(SplashScreen.this, "Sincronizado!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(SplashScreen.this, "Falha na sincronização", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }
}

