import { Alert, Snackbar as MuiSnackbar ,Slide, SnackbarCloseReason } from "@mui/material";

export default function Snackbar({ 
  children, 
  open, 
  setOpen 
}: { 
  children: React.ReactNode, 
  open: boolean, 
  setOpen: (open: boolean) => void
}): React.JSX.Element {
  
  const handleSnackbarClose = (
    _event: React.SyntheticEvent | Event,
    reason?: SnackbarCloseReason,
  ) => {
    if (reason === 'clickaway') {
      return;
    }

    setOpen(false);
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
        severity="success" 
        sx={{ 
          width: '100%',
          color: 'var(--color-text-primary)',
          backgroundColor: 'var(--color-background-secondary)'
        }}
      >
        {children}
      </Alert>
    </MuiSnackbar>
  );
};