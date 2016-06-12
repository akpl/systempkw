var liczbaOkregow = Object.keys(mapData).length;
for(var i = 1; i <= liczbaOkregow; i++) {
    var okreg = $('#okreg' + i);
    var dane = mapData[i];
    okreg.tooltip({title: dane.name, container:'body'});
    okreg.popover({title: dane.name, content: dane.details, container:'body'});
}