package com.example.tshare.model

data class recyclerTaxiShare(var from : String ?= null,
                             var to : String ?= null,
                             var date : String ?= null,
                             var time : String ?= null,
                             var timeZone : String ?= null,
                             var preference : String ?= null,
                                var userId : String ?=null)
