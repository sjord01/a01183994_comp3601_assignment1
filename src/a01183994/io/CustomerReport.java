package a01183994.io;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import a01183994.data.Customer;
import a01183994.database.dao.CustomerDao;
import a01183994.database.util.ApplicationException;
import a01183994.database.util.CustomerReportUtils;

/**
 * 
 */
public class CustomerReport {
	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static class ReportColumn {
		final String header;
		final int width;
		final Function<Customer, String> valueExtractor;

		private static final int ID_WIDTH = 14;
		private static final int NAME_WIDTH = 15;
		private static final int STREET_WIDTH = 40;
		private static final int CITY_WIDTH = 30;
		private static final int POSTAL_CODE_WIDTH = 20;
		private static final int PHONE_WIDTH = 18;
		private static final int EMAIL_WIDTH = 40;
		private static final int JOIN_DATE_WIDTH = 16;

		ReportColumn(String header, int width, Function<Customer, String> valueExtractor) {
			this.header = header;
			this.width = width;
			this.valueExtractor = valueExtractor;
		}

		static final List<ReportColumn> COLUMNS = List.of(new ReportColumn("ID", ID_WIDTH, Customer::getId),
				new ReportColumn("FIRST NAME", NAME_WIDTH, Customer::getFirstName),
				new ReportColumn("LAST NAME", NAME_WIDTH, Customer::getLastName),
				new ReportColumn("STREET", STREET_WIDTH,
						customer -> CustomerReportUtils.CustomerFormatter.truncateAndReplace(customer.getStreetName(),
								STREET_WIDTH)),
				new ReportColumn("CITY", CITY_WIDTH, Customer::getCityName),
				new ReportColumn("POSTAL CODE", POSTAL_CODE_WIDTH, Customer::getPostalCode),
				new ReportColumn("PHONE", PHONE_WIDTH,
						customer -> CustomerReportUtils.CustomerFormatter.formatPhoneNumber(customer.getPhoneNumber())),
				new ReportColumn("EMAIL", EMAIL_WIDTH, Customer::getEmail), new ReportColumn("JOIN DATE",
						JOIN_DATE_WIDTH, customer -> customer.getJoinDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));

		static String createSeparator() {
			return COLUMNS.stream().map(col -> "=".repeat(col.width)).reduce((a, b) -> a + "  " + b).orElse("");
		}

		static String formatRow(String... fields) {
			StringBuilder row = new StringBuilder();
			for (int i = 0; i < fields.length; i++) {
				row.append(String.format("%-" + COLUMNS.get(i).width + "s", fields[i]));
				if (i < fields.length - 1) {
					row.append("  ");
				}
			}
			return row.toString();
		}
	}

	public static void generateReport(CustomerDao customerDao) throws ApplicationException {
		try {
			List<Customer> customers = customerDao.getAllCustomers();
			if (customers != null && !customers.isEmpty()) {
				customers.sort((c1, c2) -> c1.getJoinDate().compareTo(c2.getJoinDate()));
				System.out.println(generateReportContent(customers));
			} else {
				System.out.println("No customers to report or error occurred while reading customers.");
			}
		} catch (Exception e) {
			throw new ApplicationException("Error generating report: " + e.getMessage());
		}
	}

	private static String generateReportContent(List<Customer> customers) {
		StringBuilder report = new StringBuilder();
		report.append(formatHeaders()).append(LINE_SEPARATOR);
		report.append(ReportColumn.createSeparator()).append(LINE_SEPARATOR);
		IntStream.range(0, customers.size()).mapToObj(i -> formatCustomer(customers.get(i)))
				.forEach(line -> report.append(line).append(LINE_SEPARATOR));
		return report.toString();
	}

	private static String formatHeaders() {
		return ReportColumn.formatRow(ReportColumn.COLUMNS.stream().map(col -> col.header).toArray(String[]::new));
	}

	private static String formatCustomer(Customer customer) {
		String[] fields = ReportColumn.COLUMNS.stream().map(col -> col.valueExtractor.apply(customer))
				.toArray(String[]::new);
		return ReportColumn.formatRow(fields);
	}

	public static String formatSingleCustomer(Customer customer) {
		return formatCustomer(customer);
	}

	public static String getReportHeader() {
		return formatHeaders() + LINE_SEPARATOR + ReportColumn.createSeparator();
	}
}