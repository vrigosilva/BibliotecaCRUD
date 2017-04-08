package br.com.fiap.biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import br.com.fiap.biblioteca.Modelo.Livro;
import br.com.fiap.biblioteca.dao.LivroDAO;

public class NovoLivroActivity extends AppCompatActivity {

    private TextInputLayout tilTitulo, tilAutor, tilPaginas, tilISBN;
    private RadioGroup rgNacional;
    private RadioButton rbBrasileiro, rbEstrangeiro;
    private Livro livro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_livro);

        tilTitulo = (TextInputLayout) findViewById(R.id.tilLivroTitulo);
        tilAutor = (TextInputLayout) findViewById(R.id.tilLivroAutor);
        tilPaginas = (TextInputLayout) findViewById(R.id.tilLivroPaginas);
        tilISBN = (TextInputLayout) findViewById(R.id.tilLivroISBN);
        rgNacional = (RadioGroup) findViewById(R.id.rgLivroNacional);
        rbBrasileiro = (RadioButton) findViewById(R.id.rbLivroNacional);
        rbEstrangeiro = (RadioButton) findViewById(R.id.rbLivroEstrangeiro);

        livro = (Livro) getIntent().getSerializableExtra("livro");
        if(livro != null){
            carregaLivro(livro);
        }else{
            livro = new Livro();
        }
    }

    private void carregaLivro(Livro livro) {
        tilTitulo.getEditText().setText(livro.getTitulo());
        tilAutor.getEditText().setText(livro.getAutor());
        tilPaginas.getEditText().setText(livro.getPaginas().toString());
        tilISBN.getEditText().setText(livro.getISBN().toString());

        if(livro.isNacional()){
            rbBrasileiro.setChecked(true);
        }else{
            rbEstrangeiro.setChecked(true);
        }
    }

    public void salvarLivro(View view){

        LivroDAO dao = new LivroDAO(this);
        boolean resultado = false;

        if (!validaLivro()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
            return;
        }

        livro.setTitulo(tilTitulo.getEditText().getText().toString());
        livro.setAutor(tilAutor.getEditText().getText().toString());
        livro.setPaginas(Integer.valueOf(tilPaginas.getEditText().getText().toString()));
        livro.setISBN(Long.valueOf(tilISBN.getEditText().getText().toString()));
        livro.setNacional(rgNacional.getCheckedRadioButtonId() == R.id.rbLivroNacional);


        if(livro.getId() != null){
            resultado = dao.update(livro);
        }else{
            resultado = dao.persist(livro);
        }

        if(resultado){
            retornaParaTelaAnterior(RESULT_OK);
        }else {
            retornaParaTelaAnterior(RESULT_CANCELED);
        }
    }

    private boolean validaLivro(){
        if(tilTitulo.getEditText().getText().length() == 0)
            return false;
        if(tilAutor.getEditText().getText().length() == 0)
            return false;
        if(tilPaginas.getEditText().getText().length() == 0)
            return false;
        if(tilISBN.getEditText().getText().length() == 0)
            return false;
        return !(!rbBrasileiro.isChecked() && !rbEstrangeiro.isChecked());

    }



    public void retornaParaTelaAnterior(int result) {
        setResult(result, new Intent());
        finish();
    }

}
