# FinalTask

Есть многоэтажное здание (этажность конфигурируема). В здании есть лифты (количество конфигурируемо). На каждом этаже есть кнопки вызова "вверх" и "вниз" (общие для всех лифтов). На каждом этаже рандомно появляются люди (рандомной массы), которые хотят ехать на другой этаж (рандомный). Интенсивность генерации людей конфигурируема. 
У каждого лифта есть грузоподъемность, скорость и скорость открытия/закрытия дверей. 
У человека есть масса тела и этаж на который ему нужно.
Люди стоят в очереди на засадку в лифты (одна очередь вверх, одна вниз) не нарушая ее. Приехав на нужный этаж, человек исчезает. 

Необходимо реализовать непрерывно работающее приложение (люди появляются, вызывают лифт и едут на нужный этаж) используя многопоточность (Thread, wait, notify, sleep).
По желанию можно использовать java.util.concurrent. Так же описать выбранный алгоритм текстом (кратко). 
// тесты, maven, логгирование
// stream API
// реализовать сбор статистики (сколько людей перевезено каждым лифтом с каким этажей и на какие этажи)
// логировать основные события системы (что бы по логам можно было следить за тем, что происходит)
// рекомендация: реализовать логику управления лифтами и покрыть ее тестами без потоков, а потом подключить многопоточность