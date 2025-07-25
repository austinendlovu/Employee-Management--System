import React, { useState, useEffect } from 'react';
import { Calendar, Clock, CheckCircle } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Progress } from '@/components/ui/progress';

interface LeaveStats {
  entitledDays: number;
  usedDays: number;
  remainingDays: number;
}

interface LeaveTrackerProps {
  refreshTrigger?: number;
}

export const LeaveTracker: React.FC<LeaveTrackerProps> = ({ refreshTrigger }) => {
  const [leaveStats, setLeaveStats] = useState<LeaveStats | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetchLeaveStats();
  }, [refreshTrigger]);

  const fetchLeaveStats = async () => {
    try {
      setIsLoading(true);
      const token = localStorage.getItem('authToken');
      const response = await fetch('http://localhost:8080/api/employee/leaves/stats', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setLeaveStats(data);
        // Store in localStorage for persistence
        localStorage.setItem('leaveStats', JSON.stringify(data));
      }
    } catch (error) {
      console.error('Failed to fetch leave stats:', error);
      // Try to load from localStorage if API fails
      const stored = localStorage.getItem('leaveStats');
      if (stored) {
        setLeaveStats(JSON.parse(stored));
      }
    } finally {
      setIsLoading(false);
    }
  };

  // Load from localStorage on component mount for immediate display
  useEffect(() => {
    const stored = localStorage.getItem('leaveStats');
    if (stored) {
      setLeaveStats(JSON.parse(stored));
    }
  }, []);

  if (isLoading && !leaveStats) {
    return (
      <div className="grid gap-4 md:grid-cols-3">
        {[1, 2, 3].map((i) => (
          <Card key={i} className="bg-glass border-glass backdrop-blur-xl">
            <CardContent className="p-6">
              <div className="animate-pulse">
                <div className="h-4 bg-muted rounded w-1/2 mb-2"></div>
                <div className="h-8 bg-muted rounded w-3/4"></div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    );
  }

  if (!leaveStats) return null;

  const usagePercentage = (leaveStats.usedDays / leaveStats.entitledDays) * 100;

  return (
    <div className="space-y-6 animate-fade-in">
      <div>
        <h2 className="text-xl font-semibold text-foreground mb-2">Leave Overview</h2>
        <p className="text-muted-foreground">Track your leave entitlement and usage</p>
      </div>
      
      <div className="grid gap-4 md:grid-cols-3">
        <Card className="bg-glass border-glass backdrop-blur-xl">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Entitled
            </CardTitle>
            <Calendar className="h-4 w-4 text-primary" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-foreground">
              {leaveStats.entitledDays}
            </div>
            <p className="text-xs text-muted-foreground">
              days per year
            </p>
          </CardContent>
        </Card>

        <Card className="bg-glass border-glass backdrop-blur-xl">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Days Taken
            </CardTitle>
            <Clock className="h-4 w-4 text-orange-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-foreground">
              {leaveStats.usedDays}
            </div>
            <p className="text-xs text-muted-foreground">
              {usagePercentage.toFixed(1)}% used
            </p>
          </CardContent>
        </Card>

        <Card className="bg-glass border-glass backdrop-blur-xl">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Remaining
            </CardTitle>
            <CheckCircle className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-foreground">
              {leaveStats.remainingDays}
            </div>
            <p className="text-xs text-muted-foreground">
              days available
            </p>
          </CardContent>
        </Card>
      </div>

      <Card className="bg-glass border-glass backdrop-blur-xl">
        <CardHeader>
          <CardTitle className="text-sm font-medium text-foreground">Leave Usage Progress</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            <div className="flex justify-between text-sm">
              <span className="text-muted-foreground">Usage: {leaveStats.usedDays} / {leaveStats.entitledDays} days</span>
              <span className="text-foreground">{usagePercentage.toFixed(1)}%</span>
            </div>
            <Progress 
              value={usagePercentage} 
              className="h-2"
            />
          </div>
        </CardContent>
      </Card>
    </div>
  );
};