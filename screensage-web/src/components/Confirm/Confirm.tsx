import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';

export default function Confirm({ 
  open, 
  setOpen, 
  title,
  description,
  onConfirm, 
  onCancel 
}: { 
  open: boolean;
  setOpen: (open: boolean) => void;
  title: string;
  description: string;
  onConfirm: () => void;
  onCancel?: () => void;
}) {

  const handleClose = () => {
    setOpen(false);
    if (onCancel) onCancel();
  };

  const handleConfirm = () => {
    onConfirm();
    setOpen(false);
  };

  return (
    <Dialog 
      open={open} 
      onClose={handleClose}
      sx={{
        '& .MuiDialog-paper': {
          backgroundColor: 'var(--color-main-black)',
          // backgroundColor: 'var(--color-background-secondary)',
          color: 'var(--color-primary-text)',
          boxShadow: 'var(--box-shadow-primary)',
          borderRadius: '8px',
          border: '1px solid var(--color-main-white)',
          padding: 'var(--spacing)'
        },
        '& .MuiDialogTitle-root': {
          color: '#ff5722',
          textShadow: 'var(--text-shadow-red)'
        },
        '& .MuiDialogContentText-root': {
          color: 'var(--color-primary-text)', 
          textShadow: 'var(--text-shadow-primary)'
        },
        // '& .MuiDialogActions-root': {
        //   borderTop: '1px solid white',
        // },
      }}
    >
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <DialogContentText>
          {description}
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} variant='outlined'>
          Cancel
        </Button>
        <Button 
          onClick={handleConfirm} 
          color='error' 
          variant='outlined'
          sx={{
            '&:hover': {
              color: '#ff5722',
              textShadow: 'var(--text-shadow-red)',
            },
          }}
        >
          Confirm
        </Button>
      </DialogActions>
    </Dialog>
  );
};
