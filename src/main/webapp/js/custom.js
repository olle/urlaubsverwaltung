// script to count number of chars in textarea 
function count(val, id) {
    document.getElementById(id).innerHTML = val.length + "/";
}

function maxChars(elem, max) {
    if (elem.value.length > max) {
        elem.value = elem.value.substring(0, max);
    }
}

function navigate(url) {
    window.location.href = url;
}

function formatNumber(number) {
    
    var num = new Number(number).toFixed(1);
    
    var nArr = num.split(".");
    
    if(nArr[1] == 0) {
        num = new Number(number).toFixed(0);
    }
    
    return num;
}

function sendGetDaysRequest(urlPrefix, startDate, toDate, dayLength, personId, el, long) {
    
    $(el).empty();
    
    if(startDate !== undefined && toDate !== undefined && startDate !== null && toDate !== null) {

    var startDateString = startDate.getFullYear() + '-' + (startDate.getMonth() + 1) + '-' + startDate.getDate();
    var toDateString = toDate.getFullYear() + '-' + (toDate.getMonth() + 1) + '-' + toDate.getDate();

    var requestUrl = urlPrefix + "/vacation/calculate";    
        
    var url = buildUrl(requestUrl, startDateString, toDateString, dayLength, personId);
        
    $.get(url, function(data) {

        var text;

        if(startDate.getFullYear() != toDate.getFullYear()) {

            var before;
            var after;

            if(startDate.getFullYear() < toDate.getFullYear()) {
                before = startDate;
                after = toDate;
            } else {
                before = toDate;
                after = startDate;
            }

            // before - 31.12.
            // 1.1.   - after

            var daysBefore;
            var daysAfter;
            var daysTotal = 0;

            var startString = before.getFullYear() + "-" + (before.getMonth() + 1) + '-' + before.getDate();
            var toString = before.getFullYear() + '-12-31';
            var url = buildUrl(requestUrl, startString, toString, dayLength, personId);

            $.get(url, function(data) {
                daysTotal += parseFloat(data);
                daysBefore = formatNumber(data);

                startString = after.getFullYear() + '-1-1';
                toString = after.getFullYear() + "-" + (after.getMonth() + 1) + '-' + after.getDate();
                url = buildUrl(requestUrl, startString, toString, dayLength, personId);

                $.get(url, function(data) {
                    daysTotal += parseFloat(data);
                    daysAfter = formatNumber(data);

                    if(long) {
                        text = "= " + formatNumber(daysTotal) + " Urlaubstag(e)";
                    } else {
                        text = formatNumber(daysTotal);
                    }
                    
                    if(long) {
                        text += "<br />(davon " + daysBefore + " in " + before.getFullYear()
                            + " und " + daysAfter + " in " + after.getFullYear() + ")";  
                    } else {
                        text += "<br />(" + before.getFullYear() + ": " + daysBefore + ", " 
                            + after.getFullYear() + ": " + daysAfter + ")";
                    }
                    
                    $(el).html(text);
                });

            });

        } else {

            if(long) {
                text = "= " + formatNumber(data) + " Urlaubstag(e)";
            } else {
                text = formatNumber(data);
            }

            $(el).html(text); 
        }

    });
        
    }
    
}

function buildUrl(urlPrefix, startDate, endDate, dayLength, personId) {

    return urlPrefix + "?from=" + startDate + "&to=" + endDate + "&length=" + dayLength + "&person=" + personId; 
    
}

// sortable tables
$(document).ready(function()
    {

        $.tablesorter.addParser({
            id: 'germanDate',
            is: function(s) {
                return false;
            },
            format: function(s) {
                var d;
                if(s.length > 10) {
                    d = s.substring(0, 9);  
                } else if(s.length == 10) {
                    d = s;
                } else {
                    return 0;
                }
                
                var a = d.split('.');
                a[1] = a[1].replace(/^[0]+/g,"");
                return new Date(a.reverse().join("/")).getTime();
            },
            type: 'numeric'
        });

        $.tablesorter.addParser({
            id: 'commaNumber',
            is: function(s){
                return false;
            },
            format: function(s) {

                var reg = new RegExp("[0-9]+");
                
                if(reg.test(s)) {
                    s = s.replace(/[\,\.]/g,'.');

                    // possible that string is sth like that: 30 + 2
                    return eval(s);  
                } else {
                    return 0;
                }
                
            },
            type: 'numeric'
        });

    }
);

function checkSonderurlaub(value) {

    if(value === "SPECIALLEAVE") {
        $('#special-leave-modal').modal("toggle"); 
    }

}

// thanks to http://www.jaqe.de/2009/01/16/url-parameter-mit-javascript-auslesen/
function getUrlParam(name)
{
    name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");

    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.href);

    if (results == null)
        return "";
    else
        return results[1];
}

// set expandable behavior on text areas
$(document).ready(function()
    {
        var standardNumberOfRows = 1;
        var expandedNumberOfRows = 4;

        $("textarea").focus(function() {
            this.rows = expandedNumberOfRows;
        });

        $("textarea").blur(function() {

            if(this.value == "") {
                this.rows = standardNumberOfRows;
            }
        });

    }
);

// toggling of full/half day in app form
$(document).ready(function()
    {
        $(".dayLength-full").click(function() {
            $('.full-day').show();
            $('.half-day').hide();
        });

        $(".dayLength-half").click(function() {
            $('.full-day').hide();
            $('.half-day').show();
        });

    }
);

// hide every feedback after a certain time
$(document).ready(function()
    {
        setInterval(hideFeedback, 5000);
    }
);

function hideFeedback() {

    $(".feedback").slideUp();

}
