package ch.unige.tpgcrowd.util;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import ch.unige.tpgcrowd.manager.ITPGLinesColors;
import ch.unige.tpgcrowd.manager.TPGManager;
import ch.unige.tpgcrowd.model.Color;
import ch.unige.tpgcrowd.model.LineColorList;
import ch.unige.tpgcrowd.net.listener.TPGObjectListener;

public class ColorStore {
	private final SharedPreferences prefs;
	private static final String SHARED_PREFERENCES =
			"LinesColorSharedPreferences";
	private static final String DEF_COLOR = "000000";//black
	
	public static void updateColors(final Context context) {
		final ITPGLinesColors cMan = TPGManager.getLinesColorsManager(context);
		cMan.getLinesColors(new TPGObjectListener<LineColorList>() {
			
			@Override
			public void onSuccess(final LineColorList results) {
				final ColorStore cs = new ColorStore(context);
				final List<Color> colors = results.getColors();
				cs.setColors(colors);
			}
			
			@Override
			public void onFailure() {
			}
		});
	}
	
	public static int getColor(final Context context, final String lineCode) {
		final ColorStore cs = new ColorStore(context);
		return cs.getColor(lineCode);
	}
	
	private ColorStore(final Context context) {
		prefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
	}
	
	private void setColors(final List<Color> colors) {
		final Editor editor = prefs.edit();
		for (final Color color : colors) {
			editor.putString(color.getLineCode(), color.getHexa());
		}
		editor.apply();
	}
	
	private int getColor(final String lineCode) {
		return android.graphics.Color.parseColor("#" + prefs.getString(lineCode, DEF_COLOR));
	}
	
	
}
