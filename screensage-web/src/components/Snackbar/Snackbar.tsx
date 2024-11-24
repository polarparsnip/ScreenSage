/* eslint-disable @typescript-eslint/no-explicit-any */
import { Alert, Snackbar as MuiSnackbar ,Slide, SnackbarCloseReason } from '@mui/material';

export default function Snackbar({ 
  children, 
  type,
  open, 
  setOpen,
  setMessage,
}: { 
  children: React.ReactNode, 
  type?: string,
  open: boolean, 
  setOpen: (open: any) => void
  setMessage?: (msg: string | null) => void
}): React.JSX.Element {
  
  const handleSnackbarClose = (
    _event: React.SyntheticEvent | Event,
    reason?: SnackbarCloseReason,
  ) => {
    if (reason === 'clickaway') {
      return;
    }

    setOpen(false);
    
    if (setMessage) {
      setMessage(null);
    }
  };

  return (
    <MuiSnackbar
      open={open}
      autoHideDuration={5000} // 5 seconds
      onClose={handleSnackbarClose}
      TransitionComponent={Slide}
    >
      <Alert
        onClose={handleSnackbarClose} 
        severity={
          type == 'success' 
            ? 'success' 
            : type == 'error' 
              ? 'error' 
              : type == 'warning' 
              ? 'warning' 
              : 'info'
        }
        sx={{ 
          width: '100%',
          color: 'var(--color-text-primary)',
          backgroundColor: 'var(--color-background-secondary)',
          outline: '1px solid var(--color-main-white)',
          textShadow:  'none',
          marginBottom: 'var(--spacing)'
        }}
      >
        {children}
      </Alert>
    </MuiSnackbar>
  );
};