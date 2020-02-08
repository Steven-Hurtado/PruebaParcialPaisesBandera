package com.example.pruebaparcialpaises;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import WebServices.Asynchtask;
import WebServices.WebService;

public class MainActivity extends AppCompatActivity implements Asynchtask {

    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.lvPaises);
        ejecutar();
    }
    private void ejecutar(){
        Map<String, String> data = new HashMap<String, String>();
        WebService ws= new WebService("http://www.geognos.com/api/en/countries/info/all.json", data, MainActivity.this, MainActivity.this  );
        ws.execute();
    }
    @Override
    public void processFinish(String result) throws JSONException {
        parseo(result);
    }
    private void parseo(String result) throws JSONException {
        List<Paises> listarpais = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(result);
        JSONObject jresults = jsonObject.getJSONObject("Results");
        Iterator<?> iterator = jresults.keys();
        while (iterator.hasNext()){
            String key =(String)iterator.next();
            JSONObject jpais = jresults.getJSONObject(key);
            Paises pais = new Paises();
            pais.setNombres(jpais.getString("Name"));
            JSONObject jCountryCodes = jpais.getJSONObject("CountryCodes");
            pais.setCodigoISO(jCountryCodes.getString("iso2"));
            listarpais.add(pais);
        }
        Adaptador adaptadorpais = new Adaptador(this,listarpais);
        list.setAdapter(adaptadorpais);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Informacion.class);
                Bundle b = new Bundle();
                b.putString("codISO", ((Paises)parent.getItemAtPosition(position)).getCodigoISO());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
