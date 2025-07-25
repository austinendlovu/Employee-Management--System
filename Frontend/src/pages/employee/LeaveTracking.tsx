import React from 'react';
import { LeaveTracker } from '@/components/LeaveTracker';

export const EmployeeLeaveTracking = () => {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-foreground">Leave Tracking</h1>
        <p className="text-muted-foreground">Monitor your leave balance and usage</p>
      </div>
      
      <LeaveTracker />
    </div>
  );
};