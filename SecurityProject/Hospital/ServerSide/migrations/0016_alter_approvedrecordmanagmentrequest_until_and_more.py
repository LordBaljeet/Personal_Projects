# Generated by Django 4.2.1 on 2023-06-16 10:03

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ServerSide', '0015_alter_admin_user_alter_adminwaitinglist_waitingadmin_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='approvedrecordmanagmentrequest',
            name='until',
            field=models.DateTimeField(default=datetime.datetime(2023, 6, 16, 11, 3, 56, 481239, tzinfo=datetime.timezone.utc), verbose_name='until'),
        ),
        migrations.AlterField(
            model_name='approveduploadrequest',
            name='until',
            field=models.DateTimeField(default=datetime.datetime(2023, 6, 16, 11, 3, 56, 482240, tzinfo=datetime.timezone.utc), verbose_name='until'),
        ),
    ]
