function searchTable(inputVal) {
    var table = $('#datatable');
    var foundAnything = true;

    table.find('tr').each(function(index, row) {
        var allCells = $(row).find('td');
        if (allCells.length > 0) {
            var found = false;
            allCells.each(function(index, td) {
                var regExp = new RegExp(inputVal, 'i');
                if (regExp.test($(td).text())) {
                    found = true;
                    return false;
                }
            });
            if (found === true) {
                foundAnything = false;
                $(row).show();
            } else {
                $(row).hide();
            }
        }
    });

    $("#no-record").hide();
    if (foundAnything === true) {
        $("#no-record").show();
    }
}

$(document).ready(function() {
    $('#search').keyup(function() {
        searchTable($(this).val());
    });
});