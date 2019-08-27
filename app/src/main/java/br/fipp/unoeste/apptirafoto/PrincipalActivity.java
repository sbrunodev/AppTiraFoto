package br.fipp.unoeste.apptirafoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class PrincipalActivity extends AppCompatActivity {
    private static final int REQUEST_FOTO = 1000;
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        img = (ImageView)findViewById(R.id.imgview);
        Button b = (Button) findViewById(R.id.btfoto);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
            }
        });
    }
    private void tirarFoto()
    {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!=null)
            startActivityForResult(intent,REQUEST_FOTO);
        else
            Toast.makeText(getApplicationContext(),
                    "O seu dispositivo não possui captura de imagens",
                    Toast.LENGTH_SHORT).show();
    }

    // evento executado no fechamento do app lançado anteriormente (camera android)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOTO)
            if (resultCode== Activity.RESULT_OK)  // confirmou
            {
                Bundle extra = data.getExtras();
                Bitmap imagebitmap = (Bitmap) extra.get("data");
                //img.setImageBitmap(imagebitmap);

                File file=getFilePrivate (this,"fototmp.jpg");
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(file);
                    imagebitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();  fos.close();
                } catch (Exception e)
                {   Toast.makeText(getApplicationContext(), "Erro " + e, Toast.LENGTH_LONG).show();  }
                Toast.makeText(getApplicationContext(),"Imagem criada em: "+file.getAbsolutePath(),
                               Toast.LENGTH_LONG).show();
                img.setImageURI(Uri.fromFile(file));

            }
            else //cancelou
               img.setImageResource(R.mipmap.ic_launcher);

    }
    public File getFilePrivate(Context context, String myfile)
    {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), myfile);
        return file;
    }

}
