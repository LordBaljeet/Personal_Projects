# Generated by Django 4.2.1 on 2023-06-08 16:15

import datetime
from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('ServerSide', '0008_medicalrecord_public_key_medicalrecord_signature_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='approvedrecordmanagmentrequest',
            name='until',
            field=models.DateTimeField(default=datetime.datetime(2023, 6, 8, 17, 15, 42, 707968, tzinfo=datetime.timezone.utc), verbose_name='until'),
        ),
        migrations.AlterField(
            model_name='approveduploadrequest',
            name='until',
            field=models.DateTimeField(default=datetime.datetime(2023, 6, 8, 17, 15, 42, 708984, tzinfo=datetime.timezone.utc), verbose_name='until'),
        ),
        migrations.CreateModel(
            name='UserLoginFail',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('connectionTime', models.DateTimeField(default=django.utils.timezone.now, verbose_name='connectionTime')),
                ('times', models.PositiveSmallIntegerField(verbose_name='times')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.PROTECT, to=settings.AUTH_USER_MODEL)),
            ],
        ),
    ]
