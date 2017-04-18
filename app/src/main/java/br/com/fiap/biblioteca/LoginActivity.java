package br.com.fiap.biblioteca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import br.com.fiap.biblioteca.Modelo.Usuario;
import br.com.fiap.biblioteca.dao.UsuarioDAO;

public class LoginActivity extends AppCompatActivity {


    private TextInputLayout tilLogin;
    private TextInputLayout tilSenha;
    private CheckBox cbManterConectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilLogin = (TextInputLayout) findViewById(R.id.tilLogin);
        tilSenha = (TextInputLayout) findViewById(R.id.tilSenha);
        cbManterConectado = (CheckBox) findViewById(R.id.cbManterConectado);

        if (isConectado()){
            iniciarApp();
        }
    }

    private boolean isConectado() {
        SharedPreferences shared = getSharedPreferences(C.KEY_APP_PREFERENCES,MODE_PRIVATE);
        String login = shared.getString(C.KEY_LOGIN, "");
        return !login.equals("");
    }

    private void iniciarApp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    //Método que será chamado no onclick do botao
    public void logar(View v){
        if(isLoginValido()){
            if(cbManterConectado.isChecked()){
                manterConectado();
            }
            iniciarApp();
        }
    }

    // Valida o login
    private boolean isLoginValido() {
        String login = tilLogin.getEditText().getText().toString();
        String senha = tilSenha.getEditText().getText().toString();

        Usuario usuario = new UsuarioDAO(this).getBy(login, senha);

        if(usuario!= null) {
            return true;
        } else {
            Toast.makeText(this, "Login e ou Senha inválidos!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void manterConectado(){
        String login = tilLogin.getEditText().getText().toString();
        SharedPreferences pref = getSharedPreferences(C.KEY_APP_PREFERENCES,  MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(C.KEY_LOGIN, login);
        editor.apply();
    }


}
