package com.sevtinge.cemiuiler.module.hook.systemui.controlcenter

import android.content.res.Configuration
import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.sevtinge.cemiuiler.R
import com.sevtinge.cemiuiler.module.base.BaseHook

import com.sevtinge.cemiuiler.utils.Helpers

import de.robv.android.xposed.XposedHelpers;

class QSGrid : BaseHook() {
    override fun init() {
        val cols = mPrefsMap.getInt("system_control_center_old_qs_columns", 2)
        val rows = mPrefsMap.getInt("system_control_center_old_qs_rows", 1)
        var colsRes = R.integer.quick_settings_num_columns_3
        var rowsRes = R.integer.quick_settings_num_rows_4

        when (cols) {
            3 -> colsRes = R.integer.quick_settings_num_columns_3
            4 -> colsRes = R.integer.quick_settings_num_columns_4
            5 -> colsRes = R.integer.quick_settings_num_columns_5
            6 -> colsRes = R.integer.quick_settings_num_columns_6
            7 -> colsRes = R.integer.quick_settings_num_columns_7
        }

        when (rows) {
            2 -> rowsRes = R.integer.quick_settings_num_rows_2
            3 -> rowsRes = R.integer.quick_settings_num_rows_3
            4 -> rowsRes = R.integer.quick_settings_num_rows_4
            5 -> rowsRes = R.integer.quick_settings_num_rows_5
        }

        val mRowsHorizontal = R.integer.quick_settings_num_rows_2

        if (pluginLoader == null) {
            pluginLoader = it.result as ClassLoader
        }
        Helpers.findAndHookMethod(
            "com.android.systemui.qs.MiuiTileLayout",
            pluginLoader,
            "updateColumns",
            object : MethodHook() {
                override fun after(param: MethodHookParam) {
                    XposedHelpers.setObjectField (
                        param.thisObject,
                        "mColumns",
                        colsRes
                    )
                }
            }
        )

        Helpers.findAndHookMethod(
            "com.android.systemui.qs.MiuiTileLayout",
            pluginLoader,
            "updateResources",
            object : MethodHook() {
                override fun after(param: MethodHookParam) {
                    XposedHelpers.setObjectField (
                        param.thisObject,
                        "mMaxAllowedRows",
                        rowsRes
                    )
                }
            }
        )
        /*
        findMethod("com.android.systemui.qs.MiuiTileLayout") {
            name == "updateColumns"
        }.hookAfter {
            it.thisObject.putObject("mColumns", colsRes)
        }

        findMethod("com.android.systemui.qs.MiuiTileLayout") {
            name == "updateResources"
        }.hookAfter {
            val viewGroup = it.thisObject as ViewGroup
            val mConfiguration: Configuration = viewGroup.context.resources.configuration
            if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                viewGroup.putObject("mMaxAllowedRows", rowsRes)
            } else {
                viewGroup.putObject("mMaxAllowedRows", mRowsHorizontal)
            }
            viewGroup.requestLayout()
        }
        */
    }
}