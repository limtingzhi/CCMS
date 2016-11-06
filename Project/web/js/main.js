/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function insertParams(params) {
    var kvp = document.location.search.substr(1).split('&');

    for (var i = 0; i < params.length; i++) {

        key = encodeURI(params[i].key);
        value = encodeURI(params[i].value);
        var j = kvp.length;
        var x;
        while (j--) {
            x = kvp[j].split('=');

            if (x[0] == key) {
                x[1] = value;
                kvp[j] = x.join('=');
                break;
            }
        }

        if (j < 0) {
            kvp[kvp.length] = [key, value].join('=');
        }
    }

    return kvp.join('&');
}

function getCurLocation() {
    return location.protocol + '//' + location.host + location.pathname;
}

function toggle(source) {
    checkboxes = document.getElementsByName('selectedItems');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
        checkboxes[i].checked = source.checked;
    }
}

function archiveRow(table) {
    var arr = new Array();
    for (var rowi = table.rows.length; rowi-- > 0; ) {
        var row = table.rows[rowi];
        var inputs = row.getElementsByTagName('input');
        for (var inputi = inputs.length; inputi-- > 0; ) {
            var input = inputs[inputi];

            if (input.type === 'checkbox' && input.checked) {
                row.parentNode.removeChild(row);
                arr.push(input.value);
                break;
            }
        }
    }
    document.location = getCurLocation() + "?" + insertParams([{
            key: 'ordersToArchive',
            value: arr
        }]) + "#actions";
}

function undoneRow(table) {
    var arr = new Array();
    for (var rowi = table.rows.length; rowi-- > 0; ) {
        var row = table.rows[rowi];
        var inputs = row.getElementsByTagName('input');
        for (var inputi = inputs.length; inputi-- > 0; ) {
            var input = inputs[inputi];

            if (input.type === 'checkbox' && input.checked) {
                row.parentNode.removeChild(row);
                arr.push(input.value);
                break;
            }
        }
    }
    document.location = getCurLocation() + "?" + insertParams([{
            key: 'ordersToUnmark',
            value: arr
        }]) + "#actions";
}

function searchTable(inputVal) {
    var table = $('#datatable');
    table.find('tr').each(function(index, row)
    {
        var allCells = $(row).find('td');
        if (allCells.length > 0)
        {
            var found = false;
            allCells.each(function(index, td)
            {
                var regExp = new RegExp(inputVal, 'i');
                if (regExp.test($(td).text()))
                {
                    found = true;
                    return false;
                }
            });
            if (found == true)
                $(row).show();
            else
                $(row).hide();
        }
    });
}

$(document).ready(function() {
    $('#search').keyup(function() {
        searchTable($(this).val());
    });
});