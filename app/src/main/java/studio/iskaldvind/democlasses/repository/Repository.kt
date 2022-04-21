package studio.iskaldvind.democlasses.repository

import studio.iskaldvind.democlasses.data.classes
import studio.iskaldvind.democlasses.data.examTime
import studio.iskaldvind.democlasses.data.homeworks
import studio.iskaldvind.democlasses.data.name
import studio.iskaldvind.democlasses.model.AppClass
import studio.iskaldvind.democlasses.model.Homework

class Repository: IRepository {
    override fun getName(): String =
        name

    override fun getExamTime(): Long =
        examTime

    override fun getClasses(): List<AppClass> =
        classes

    override fun getHomeworks(): List<Homework> =
        homeworks
}