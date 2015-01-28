package com.unlar.controlador;

import java.util.ArrayList;

import com.unlar.modelo.Area;
import com.unlar.modelo.Bloque;
import com.unlar.modelo.Dependencia;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemDependenciaAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Dependencia> listDependencias;
	protected ArrayList<Bloque> listBloques;
	protected ArrayList<Area> listAreas;
	protected Area area;
	
	public ItemDependenciaAdapter(Activity activity, ArrayList<Dependencia> items, ArrayList<Bloque> bloqs, ArrayList<Area> areas) {
		this.activity = activity;
		this.listDependencias = items;
		listBloques = bloqs;
		listAreas = areas;
	}

	@Override
	public int getCount() {
		return listDependencias.size();
	}

	@Override
	public Object getItem(int position) {
		return listDependencias.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listDependencias.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.item_list_dependencia, null);
		}

		Dependencia itemDep = listDependencias.get(position);
		
		TextView txtNombre = (TextView) vi.findViewById(R.id.txtnombredep);
		txtNombre.setText("Nombre: " + itemDep.getNombre());

		TextView txtCodigo = (TextView) vi.findViewById(R.id.txtcodigodep);
		txtCodigo.setText("Código: " + itemDep.getCodigo());
		if(listBloques==null){
			Log.e("errorlistadobloques", ""+listBloques);
		}
		for (int i = 0; i < listBloques.size(); i++) {
			if(listDependencias.get(position).getId_bloque()==listBloques.get(i).getId()){
				for (int j = 0; j < listAreas.size(); j++) {
					if(listAreas.get(j).getId()==listBloques.get(i).getId_area()){
						area= listAreas.get(j);
					}
				}
			}
			
		}
		TextView txtArea = (TextView) vi.findViewById(R.id.txtareadep);
		txtArea.setText("Área: "+area.getNombre());

		return vi;
	}
}
