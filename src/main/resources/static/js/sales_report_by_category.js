// Sales Report by Category
var data;
var chartOptions;

$(document).ready(function() {
	setupButtonEventHandlers("_category", loadSalesReportByDateForCategory);	
});

// function loadSalesReportByDateForCategory() {
// 	requestURL = contextPath + "reports/sales_by_category/last_7_days";
//
// 	$get(requestURL, function (responseJSON) {
// 		console.log(responseJSON);
// 	});
// }

function loadSalesReportByDateForCategory(period) {
	if (period == "custom") {
		startDate = $("#startDate_category").val();
		endDate = $("#endDate_category").val();
		
		requestURL = contextPath + "reports/category/" + startDate + "/" + endDate;
	} else {
		requestURL = contextPath + "reports/category/" + period;		
	}
	
	$.get(requestURL, function(responseJSON) {
		prepareChartDataForSalesReportByCategory(responseJSON);
		customizeChartForSalesReportByCategory();
		formatChartData(data, 1);
		drawChartForSalesReportByCategory(period);
		setSalesAmount(period, '_category', "Tổng sản phẩm bán ra");
	});
}

function prepareChartDataForSalesReportByCategory(responseJSON) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Category');
	data.addColumn('number', 'Gross Sales');
	// data.addColumn('number', 'Net Sales');
	
	totalGrossSales = 0;
	// totalNetSales = 0.0;
	totalItems = 0;
	
	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossTypeSales]]);
		totalGrossSales += parseFloat(reportItem.grossTypeSales);
		totalItems += parseInt(reportItem.productCount);
	});
}

function customizeChartForSalesReportByCategory() {
	chartOptions = {
		height: 360, legend: {position: 'right'}
	};
}

function drawChartForSalesReportByCategory() {
	var salesChart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
	salesChart.draw(data, chartOptions);
}
