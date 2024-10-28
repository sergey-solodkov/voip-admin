import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  standalone: true,
  name: 'yesNo',
})
export default class YesNoPipe implements PipeTransform {
  transform(value: any): string {
    return value ? 'global.value.yes' : 'global.value.no'; // TODO translate
  }
}
