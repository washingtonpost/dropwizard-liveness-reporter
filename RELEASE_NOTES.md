# Dropwizard-Liveness-Reporter Release Notes

## 1.4.0 2016/05/27

* Upgrading from com.indeed:java-dogstatd-client:2.0.9 to 2.0.16
* Added support for datadog tags

## 1.3.0 2015/12/17

* Upgrading from com.indeed:java-dogstatd-client:2.0.7 to 2.0.9

## 1.2.0 2015/11/06

* Upgrading to library versions compatible with dropwizard-0.9.1
  (specifically, Jackson 2.5.1 -> 2.6.2 and slf4j 1.7.10 -> 1.7.12)

## 1.1.0 2015/10/13

* Added additional constructors/builders to facilitate re-using a StatsDClient

## 1.0.0 2015/09/22

* Initial implementation of the simple thread-writes-a-"1" strategy
