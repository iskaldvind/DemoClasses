package studio.iskaldvind.democlasses.repository

import studio.iskaldvind.democlasses.model.AppClass
import studio.iskaldvind.democlasses.model.Homework

interface IRepository {
    fun getName(): String
    fun getExamTime(): Long
    fun getClasses(): List<AppClass>
    fun getHomeworks(): List<Homework>
}