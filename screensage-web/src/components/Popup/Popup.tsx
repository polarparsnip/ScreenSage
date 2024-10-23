import s from './Popup.module.scss'
import { Button } from '@mui/material';
export function Popup({ message, onClose }: { message: string, onClose: () => void }) {
  return (
    <div className={s.overlay} onClick={onClose}>
      <div className={s.popup} onClick={(e) => e.stopPropagation()}>
        <div>{message}</div>
        <div><Button variant='outlined' className={s.closeButton} onClick={onClose}>Close</Button></div>
      </div>
    </div>
  );
}
