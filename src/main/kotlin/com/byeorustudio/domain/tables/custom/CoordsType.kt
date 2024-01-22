package com.byeorustudio.domain.tables.custom

import net.postgis.jdbc.PGgeometry
import net.postgis.jdbc.geometry.Geometry
import net.postgis.jdbc.geometry.Point
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table.Dual.registerColumn

class PointColumnType : ColumnType() {
    override fun sqlType(): String = "geometry"

    override fun valueFromDB(value: Any): Any {
        return when (value) {
            is PGgeometry -> value.geometry as Geometry
            else -> value
        }
    }

    override fun notNullValueToDB(value: Any): Any {
        return when (value) {
            is Geometry -> PGgeometry(value)
            else -> value
        }
    }
}

fun point(name: String): Column<Point> = registerColumn(name, PointColumnType())