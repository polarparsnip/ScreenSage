import s from './Popup.module.scss'
import { Button } from '@mui/material';

export function Popup({ message, onClose, shadowColor }: { message: string, onClose: () => void, shadowColor?: string }) {
  return (
    <div className={s.overlay} onClick={onClose}>
      <div 
        className={
          `${s.popup} ${shadowColor == 'green' ? s.green : shadowColor == 'red' ? s.red : s.default}`
        } 
        onClick={(e) => e.stopPropagation()}
      >
        <div>{message}</div>
        <div><Button variant='outlined' className={s.closeButton} onClick={onClose}>Close</Button></div>
      </div>
    </div>
  );
}
