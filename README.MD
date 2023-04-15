1 find the relatively most healthy route

URL: 
```
http://127.0.0.1:8082/v1/route?coordinates=-96.7533417,32.9950066,-96.7504163,32.9862204&sensitivePollutants=1,2,3
```

HTTP Method: Get

Input Parameters:
```
coordinates=startPointLongitude,startPointLatitude,endPointLongitude,endPointLatitude
sensitivePollutants: this is a optional parameter. it's used to represent the user's sensitive pollutants preferences.
you can pass any combination of 1,2,3,4,5,6. those number means:
    PM25(1),
    PM10(2),
    O3(3),
    SO2(4),
    CO(5),
    NO2(6);
For example: 1,2,3 means the user is sensitive to PM25,PM10,O3.
```

Output Parameters:
```
{
    "code": 1,
    "message": "success",
    "data": [
        {
            "longitude": -96.75324450000001,
            "latitude": 32.994696600000005
        },
        {
            "longitude": -96.7531805,
            "latitude": 32.9946627
        },
        {
            "longitude": -96.7526713,
            "latitude": 32.994233300000005
        },
        {
            "longitude": -96.7526421,
            "latitude": 32.9941866
        },
        {
            "longitude": -96.7526352,
            "latitude": 32.9941444
        },
        {
            "longitude": -96.752638,
            "latitude": 32.993800300000004
        },
        {
            "longitude": -96.75263890000001,
            "latitude": 32.993307200000004
        },
        {
            "longitude": -96.75244310000001,
            "latitude": 32.9933069
        },
        {
            "longitude": -96.75184270000001,
            "latitude": 32.9933034
        },
        {
            "longitude": -96.7518428,
            "latitude": 32.993194700000004
        },
        {
            "longitude": -96.7518428,
            "latitude": 32.9931368
        },
        {
            "longitude": -96.7514974,
            "latitude": 32.993134500000004
        },
        {
            "longitude": -96.7510776,
            "latitude": 32.9931302
        },
        {
            "longitude": -96.750951,
            "latitude": 32.9931294
        },
        {
            "longitude": -96.75095400000001,
            "latitude": 32.992963
        },
        {
            "longitude": -96.75095400000001,
            "latitude": 32.9928572
        },
        {
            "longitude": -96.75095400000001,
            "latitude": 32.992134
        },
        {
            "longitude": -96.75095490000001,
            "latitude": 32.9920503
        },
        {
            "longitude": -96.750956,
            "latitude": 32.991944700000005
        },
        {
            "longitude": -96.7509569,
            "latitude": 32.9918588
        },
        {
            "longitude": -96.750962,
            "latitude": 32.991385
        },
        {
            "longitude": -96.75096500000001,
            "latitude": 32.9913047
        },
        {
            "longitude": -96.750968,
            "latitude": 32.9911888
        },
        {
            "longitude": -96.7509684,
            "latitude": 32.990769400000005
        },
        {
            "longitude": -96.7509685,
            "latitude": 32.990713500000005
        },
        {
            "longitude": -96.75096900000001,
            "latitude": 32.9906494
        },
        {
            "longitude": -96.75096900000001,
            "latitude": 32.9906148
        },
        {
            "longitude": -96.7509687,
            "latitude": 32.9902067
        },
        {
            "longitude": -96.7509722,
            "latitude": 32.9898238
        },
        {
            "longitude": -96.7509725,
            "latitude": 32.989783800000005
        },
        {
            "longitude": -96.75097330000001,
            "latitude": 32.9896775
        },
        {
            "longitude": -96.75097690000001,
            "latitude": 32.989156
        },
        {
            "longitude": -96.75097840000001,
            "latitude": 32.9889348
        },
        {
            "longitude": -96.7509828,
            "latitude": 32.988304400000004
        },
        {
            "longitude": -96.7509849,
            "latitude": 32.987991300000004
        },
        {
            "longitude": -96.7509852,
            "latitude": 32.9879466
        },
        {
            "longitude": -96.75098870000001,
            "latitude": 32.9873873
        },
        {
            "longitude": -96.7509921,
            "latitude": 32.9868529
        },
        {
            "longitude": -96.75099300000001,
            "latitude": 32.986704
        },
        {
            "longitude": -96.7509936,
            "latitude": 32.9866382
        },
        {
            "longitude": -96.7505924,
            "latitude": 32.9866296
        },
        {
            "longitude": -96.75056160000001,
            "latitude": 32.9866498
        },
        {
            "longitude": -96.75052500000001,
            "latitude": 32.986660300000004
        },
        {
            "longitude": -96.75048620000001,
            "latitude": 32.9866593
        },
        {
            "longitude": -96.75045530000001,
            "latitude": 32.9866459
        },
        {
            "longitude": -96.7504256,
            "latitude": 32.986623900000005
        }
    ]
}
```
Note that the code is an Enum type: 1-success, 0-failed. The following is a failed example:
```
{
    "code": 0,
    "message": "Parameter error",
    "data": null
}
```


2 get polygons by giving a northern latitude, a southern latitude, a western longitude, and an eastern longitude
URL:
```
127.0.0.1:8082/v1/visualization/polygons?boundary=32.9950066,32.9862204,-96.7533417,-96.7504163
```

HTTP Method: Get

Input Parameters:
```
boundary=northernLatitude,southernLatitude,westernLongitude,easternLongitude
```

Output Parameters:
```
{
    "code": 1,
    "message": "success",
    "data": {
        "polygons": [
            {
                "northernLatitude": 32.9950066,
                "southernLatitude": 32.9862204,
                "westernLongitude": -96.7533417,
                "easternLongitude": -96.7504163,
                "measurements": [
                    {
                        "pollutantId": "PM25",
                        "value": 10,
                        "timestamp": "2023-04-15T15:24:30.400Z"
                    },
                    {
                        "pollutantId": "PM10",
                        "value": 100,
                        "timestamp": "2023-04-15T15:24:30.400Z"
                    },
                    {
                        "pollutantId": "O3",
                        "value": 200,
                        "timestamp": "2023-04-15T15:24:30.400Z"
                    },
                    {
                        "pollutantId": "SO2",
                        "value": 300,
                        "timestamp": "2023-04-15T15:24:30.400Z"
                    },
                    {
                        "pollutantId": "CO",
                        "value": 400,
                        "timestamp": "2023-04-15T15:24:30.400Z"
                    },
                    {
                        "pollutantId": "NO2",
                        "value": 500,
                        "timestamp": "2023-04-15T15:24:30.400Z"
                    }
                ]
            }
        ]
    }
}
```