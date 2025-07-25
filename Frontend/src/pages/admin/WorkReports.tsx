import React, { useState, useEffect } from 'react';
import { Calendar, Clock, FileText, User, Filter, BarChart3, TrendingUp } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { useToast } from '@/hooks/use-toast';

interface Employee {
  id: number;
  fullName: string;
  department: string;
  position: string;
}

interface WorkReport {
  employeeName: string;
  employeeId: number;
  totalHoursWorked: number;
  totalLogsSubmitted: number;
  firstLogDate: string;
  lastLogDate: string;
  averageHoursPerDay: number;
  hoursPerDay: Record<string, number>;
}

export const AdminWorkReports = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [selectedEmployeeId, setSelectedEmployeeId] = useState<string>('');
  const [report, setReport] = useState<WorkReport | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [showFilters, setShowFilters] = useState(false);
  const [dateFilters, setDateFilters] = useState({
    from: '',
    to: ''
  });
  const { toast } = useToast();

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const token = localStorage.getItem('authToken');
      const response = await fetch('http://localhost:8080/api/admin/employees', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setEmployees(data);
      }
    } catch (error) {
      console.error('Failed to fetch employees:', error);
    }
  };

  const fetchFullReport = async (employeeId: string) => {
    if (!employeeId) return;
    
    setIsLoading(true);
    try {
      const token = localStorage.getItem('authToken');
      const response = await fetch(`http://localhost:8080/api/admin/worklogs/report/all/${employeeId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setReport(data);
        // Cache the report
        localStorage.setItem(`workReport_${employeeId}`, JSON.stringify(data));
      } else {
        toast({
          title: "No Data Found",
          description: "No work logs found for this employee.",
          variant: "destructive",
        });
      }
    } catch (error) {
      console.error('Failed to fetch full report:', error);
      toast({
        title: "Error",
        description: "Failed to fetch work report.",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const fetchFilteredReport = async () => {
    if (!selectedEmployeeId || !dateFilters.from || !dateFilters.to) {
      toast({
        title: "Missing Information",
        description: "Please select an employee and both from/to dates.",
        variant: "destructive",
      });
      return;
    }

    setIsLoading(true);
    try {
      const token = localStorage.getItem('authToken');
      const response = await fetch(
        `http://localhost:8080/api/admin/worklogs/report/${selectedEmployeeId}?from=${dateFilters.from}&to=${dateFilters.to}`,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        setReport(data);
      } else {
        toast({
          title: "No Data Found",
          description: "No work logs found for the selected date range.",
          variant: "destructive",
        });
      }
    } catch (error) {
      console.error('Failed to fetch filtered report:', error);
      toast({
        title: "Error",
        description: "Failed to fetch filtered work report.",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const clearReport = () => {
    setReport(null);
    setDateFilters({ from: '', to: '' });
  };

  // Load cached report on employee selection
  useEffect(() => {
    if (selectedEmployeeId) {
      const cached = localStorage.getItem(`workReport_${selectedEmployeeId}`);
      if (cached) {
        setReport(JSON.parse(cached));
      }
    }
  }, [selectedEmployeeId]);

  const hoursPerDayEntries = report ? Object.entries(report.hoursPerDay).sort((a, b) => new Date(b[0]).getTime() - new Date(a[0]).getTime()) : [];

  return (
    <div className="space-y-6 animate-fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground">Work Reports</h1>
          <p className="text-muted-foreground">Generate and analyze employee work reports</p>
        </div>
        <Button
          onClick={() => setShowFilters(!showFilters)}
          variant="outline"
          className="gap-2"
        >
          <Filter className="h-4 w-4" />
          {showFilters ? 'Hide Filters' : 'Show Date Filters'}
        </Button>
      </div>

      {/* Employee Selection */}
      <Card className="bg-glass border-glass backdrop-blur-xl">
        <CardHeader>
          <CardTitle className="text-foreground flex items-center gap-2">
            <User className="h-5 w-5" />
            Select Employee
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex gap-4 items-end">
            <div className="flex-1 space-y-2">
              <Label htmlFor="employee">Employee</Label>
              <Select value={selectedEmployeeId} onValueChange={setSelectedEmployeeId}>
                <SelectTrigger>
                  <SelectValue placeholder="Select an employee" />
                </SelectTrigger>
                <SelectContent>
                  {employees.map((employee) => (
                    <SelectItem key={employee.id} value={employee.id.toString()}>
                      {employee.fullName} - {employee.department}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <Button
              onClick={() => fetchFullReport(selectedEmployeeId)}
              disabled={!selectedEmployeeId || isLoading}
              className="gap-2"
            >
              <BarChart3 className="h-4 w-4" />
              Generate Full Report
            </Button>
            {report && (
              <Button
                onClick={clearReport}
                variant="outline"
              >
                Clear Report
              </Button>
            )}
          </div>
        </CardContent>
      </Card>

      {/* Date Filters */}
      {showFilters && (
        <Card className="bg-glass border-glass backdrop-blur-xl animate-scale-in">
          <CardHeader>
            <CardTitle className="text-foreground">Date Range Filter</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid gap-4 md:grid-cols-3">
              <div className="space-y-2">
                <Label htmlFor="fromDate">From Date</Label>
                <Input
                  id="fromDate"
                  type="date"
                  value={dateFilters.from}
                  onChange={(e) => setDateFilters({...dateFilters, from: e.target.value})}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="toDate">To Date</Label>
                <Input
                  id="toDate"
                  type="date"
                  value={dateFilters.to}
                  onChange={(e) => setDateFilters({...dateFilters, to: e.target.value})}
                />
              </div>
              <div className="flex items-end">
                <Button
                  onClick={fetchFilteredReport}
                  disabled={!selectedEmployeeId || !dateFilters.from || !dateFilters.to || isLoading}
                  className="w-full gap-2"
                >
                  <Filter className="h-4 w-4" />
                  Generate Filtered Report
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Loading State */}
      {isLoading && (
        <div className="text-center py-8">
          <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
          <p className="mt-2 text-muted-foreground">Generating report...</p>
        </div>
      )}

      {/* Report Results */}
      {report && !isLoading && (
        <div className="space-y-6">
          {/* Summary Stats */}
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
            <Card className="bg-glass border-glass backdrop-blur-xl">
              <CardContent className="p-6">
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 bg-primary/20 rounded-lg flex items-center justify-center">
                    <User className="h-6 w-6 text-primary" />
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Employee</p>
                    <p className="text-lg font-bold text-foreground">{report.employeeName}</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="bg-glass border-glass backdrop-blur-xl">
              <CardContent className="p-6">
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 bg-accent/20 rounded-lg flex items-center justify-center">
                    <Clock className="h-6 w-6 text-accent" />
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Total Hours</p>
                    <p className="text-2xl font-bold text-foreground">{report.totalHoursWorked}h</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="bg-glass border-glass backdrop-blur-xl">
              <CardContent className="p-6">
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 bg-green-500/20 rounded-lg flex items-center justify-center">
                    <FileText className="h-6 w-6 text-green-500" />
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Total Logs</p>
                    <p className="text-2xl font-bold text-foreground">{report.totalLogsSubmitted}</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="bg-glass border-glass backdrop-blur-xl">
              <CardContent className="p-6">
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 bg-blue-500/20 rounded-lg flex items-center justify-center">
                    <TrendingUp className="h-6 w-6 text-blue-500" />
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Avg. Hours/Day</p>
                    <p className="text-2xl font-bold text-foreground">{report.averageHoursPerDay.toFixed(1)}h</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Date Range */}
          <Card className="bg-glass border-glass backdrop-blur-xl">
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <Calendar className="h-5 w-5 text-primary" />
                  <span className="font-medium text-foreground">Report Period</span>
                </div>
                <div className="text-right">
                  <p className="text-sm text-muted-foreground">
                    {report.firstLogDate} to {report.lastLogDate}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Hours Per Day Table */}
          <Card className="bg-glass border-glass backdrop-blur-xl">
            <CardHeader>
              <CardTitle className="text-foreground flex items-center gap-2">
                <BarChart3 className="h-5 w-5" />
                Daily Work Hours
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-auto max-h-96">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Date</TableHead>
                      <TableHead className="text-right">Hours Worked</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {hoursPerDayEntries.map(([date, hours]) => (
                      <TableRow key={date}>
                        <TableCell className="font-medium">{date}</TableCell>
                        <TableCell className="text-right">
                          <span className="inline-flex items-center gap-1">
                            <Clock className="h-4 w-4 text-accent" />
                            {hours}h
                          </span>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
              {hoursPerDayEntries.length === 0 && (
                <div className="text-center py-8">
                  <p className="text-muted-foreground">No daily work hours data available.</p>
                </div>
              )}
            </CardContent>
          </Card>
        </div>
      )}

      {/* No Report State */}
      {!report && !isLoading && (
        <div className="text-center py-12">
          <BarChart3 className="h-16 w-16 text-muted-foreground mx-auto mb-4" />
          <h3 className="text-lg font-medium text-foreground mb-2">No Report Generated</h3>
          <p className="text-muted-foreground">Select an employee and generate a report to view work statistics.</p>
        </div>
      )}
    </div>
  );
};